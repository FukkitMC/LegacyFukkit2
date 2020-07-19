package io.github.fukkitmc.fukkit.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;

import com.google.common.io.ByteStreams;
import io.github.fukkitmc.fukkit.util.LegacyCommodore;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
final class FukkitPluginClassLoader extends PluginClassLoader {
    private final Manifest manifest;
    private final URL url;
    private FukkitJavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Spigot Start
    static
    {
        try
        {
            java.lang.reflect.Method method = ClassLoader.class.getDeclaredMethod( "registerAsParallelCapable" );
            if ( method != null )
            {
                boolean oldAccessible = method.isAccessible();
                method.setAccessible( true );
                method.invoke( null );
                method.setAccessible( oldAccessible );
                org.bukkit.Bukkit.getLogger().log( java.util.logging.Level.INFO, "Set PluginClassLoader as parallel capable" );
            }
        } catch ( NoSuchMethodException ex )
        {
            // Ignore
        } catch ( Exception ex )
        {
            org.bukkit.Bukkit.getLogger().log( java.util.logging.Level.WARNING, "Error setting PluginClassLoader as parallel capable", ex );
        }
    }

    private JarFile jar;
    // Spigot End
    
    FukkitPluginClassLoader(final FukkitJavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws InvalidPluginException, MalformedURLException {
        super(loader, parent, description, dataFolder, file);
        try {
            this.jar = new JarFile(file);
            this.manifest = this.jar.getManifest();
            this.url = file.toURI().toURL();
        } catch (IOException e) {
            throw new InvalidPluginException(e);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass( String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);

                if (result != null) {
                    PluginDescriptionFile provider = ((FukkitPluginClassLoader) result.getClassLoader()).description;

                    if (provider != description
                            && !seenIllegalAccess.contains(provider.getName())) {

                        seenIllegalAccess.add(provider.getName());
                        if (plugin != null) {
                            plugin.getLogger().log(Level.WARNING, "Loaded class {0} from {1} which is not a depend, softdepend or loadbefore of this plugin.", new Object[]{name, provider.getFullName()});
                        } else {
                            // In case the bad access occurs on construction
                            loader.server.getLogger().log(Level.WARNING, "[{0}] Loaded class {1} from {2} which is not a depend, softdepend or loadbefore of this plugin.", new Object[]{description.getName(), name, provider.getFullName()});
                        }
                    }
                }
            }

            if (result == null) {
                String path = name.replace('.', '/').concat(".class");
                JarEntry entry = jar.getJarEntry(path);

                if (entry != null) {
                    byte[] classBytes;

                    try (InputStream is = jar.getInputStream(entry)) {
                        classBytes = ByteStreams.toByteArray(is);
                    } catch (IOException ex) {
                        throw new ClassNotFoundException(name, ex);
                    }

                    classBytes = processClass(description, path, classBytes);

                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ex) {
                                if (getPackage(pkgName) == null) {
                                    throw new IllegalStateException("Cannot find package " + pkgName);
                                }
                            }
                        }
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    result = defineClass(name, classBytes, 0, classBytes.length, source);
                }

                if (result == null) {
                    result = super.findClass(name);
                }

                if (result != null) {
                    loader.setClass(name, result);
                }

                classes.put(name, result);
            }
        }

        return result;
    }

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(FukkitJavaPlugin fukkitJavaPlugin) {
        Validate.notNull(fukkitJavaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(fukkitJavaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = fukkitJavaPlugin;

        fukkitJavaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }

    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        try {
            clazz = LegacyCommodore.convert(clazz);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Fatal error trying to convert " + pdf.getFullName() + ":" + path, ex);
        }

        return clazz;
    }

}
