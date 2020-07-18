package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import jline.console.ConsoleReader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow public CraftServer server;

    @Shadow public static Logger LOGGER;

    @Inject(method = "main", at = @At("HEAD"))
    private static void mainMethod(String[] args, CallbackInfo ci) {
        Main.main(args);
    }

    @Inject(method = "<init>(Ljava/io/File;Ljava/net/Proxy;Ljava/io/File;)V", at = @At("TAIL"))
    public void constructor(File gameDir, Proxy proxy, File file, CallbackInfo ci){
        try {
            ((MinecraftServer)(Object)this).reader = new ConsoleReader(System.in, System.out);
            ((MinecraftServer)(Object)this).reader.setExpandEvents(false); // Avoid parsing exceptions for uncommonly used event designators
        } catch (Throwable e) {
            try {
                // Try again with jline disabled for Windows users without C++ 2008 Redistributable
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
                System.setProperty("user.language", "en");
                Main.useJline = false;
                ((MinecraftServer)(Object)this).reader = new ConsoleReader(System.in, System.out);
                ((MinecraftServer)(Object)this).reader.setExpandEvents(false);
            } catch (IOException ex) {
                LOGGER.warn((String) null, ex);
            }
        }
    }

    @Inject(method = "method_6521", at = @At("TAIL"))
    public void postWorldLoadPlugins(CallbackInfo ci){
        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD); // CraftBukkit
    }
}
