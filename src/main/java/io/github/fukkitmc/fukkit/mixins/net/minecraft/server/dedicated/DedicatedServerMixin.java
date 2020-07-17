package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.dedicated;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import net.minecraft.server.dedicated.DedicatedPlayerManager;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerWatchdog;
import net.minecraft.server.rcon.QueryResponseHandler;
import net.minecraft.server.rcon.RconServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.storage.SaveData;
import org.bukkit.craftbukkit.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.Random;

@Environment(EnvType.SERVER)
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    @Shadow
    public abstract long getMaxTickTime();

    @Shadow
    public AbstractPropertiesHandler abstractPropertiesHandler;

    @Shadow
    public RconServer rconServer;

    @Shadow
    public QueryResponseHandler queryResponseHandler;

    @Shadow
    public abstract boolean areCommandBlocksEnabled();

    public DedicatedServerMixin(File gameDir, Proxy proxy, File file) {
        super(gameDir, proxy, file);
    }

    @Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;isOnlineMode()Z", shift = At.Shift.BEFORE))
    public void spigotLoadPlugins(CallbackInfoReturnable<Boolean> cir) {
        this.playerManager = (new DedicatedPlayerManager((DedicatedServer) (Object) this)); // Fukkit - move around
        // Fukkit start
        org.spigotmc.SpigotConfig.init((File) Main.options.valueOf("spigot-settings"));
        org.spigotmc.SpigotConfig.registerCommands();
        // Fukkit end
        server.loadPlugins();
        server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
        // Spigot End
    }

    @Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;setPlayerManager(Lnet/minecraft/server/PlayerManager;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void cancelPlayerManagerSetup(CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        if (!ServerConfigHandler.checkSuccess(this.abstractPropertiesHandler)) {
            cir.setReturnValue(false);
            return;
        } else {
            //Fukkit - Craftbukkit, how about NO
//            this.saveStorage = new SaveData(server.getWorldContainer()); // CraftBukkit - moved from MinecraftServer constructor
            long j = System.nanoTime();

            if (this.getLevelName() == null) {
                this.setLevelName(this.abstractPropertiesHandler.getOrDefault("level-name", "world"));
            }

            String s = this.abstractPropertiesHandler.getOrDefault("level-seed", "");
            String s1 = this.abstractPropertiesHandler.getOrDefault("level-type", "DEFAULT");
            String s2 = this.abstractPropertiesHandler.getOrDefault("generator-settings", "");
            long k = (new Random()).nextLong();

            if (s.length() > 0) {
                try {
                    long l = Long.parseLong(s);

                    if (l != 0L) {
                        k = l;
                    }
                } catch (NumberFormatException numberformatexception) {
                    k = (long) s.hashCode();
                }
            }

            LevelGeneratorType worldtype = LevelGeneratorType.getTypeFromName(s1);

            if (worldtype == null) {
                worldtype = LevelGeneratorType.DEFAULT;
            }

            this.shouldAnnouncePlayerAchievements();
            this.areCommandBlocksEnabled();
            this.getOpPermissionLevel();
            this.isSnooperEnabled();
            this.getNetworkCompressionThreshold();
            this.setWorldHeight(this.abstractPropertiesHandler.getIntOrDefault("max-build-height", 256));
            this.setWorldHeight((this.getWorldHeight() + 8) / 16 * 16);
            this.setWorldHeight(MathHelper.clamp(this.getWorldHeight(), 64, 256));
            this.abstractPropertiesHandler.set("max-build-height", Integer.valueOf(this.getWorldHeight()));
            DedicatedServer.LOGGER.info("Preparing level \"" + this.getLevelName() + "\"");
            this.setupWorld(this.getLevelName(), this.getLevelName(), k, worldtype, s2);
            long i1 = System.nanoTime() - j;
            String s3 = String.format("%.3fs", new Object[]{Double.valueOf((double) i1 / 1.0E9D)});

            DedicatedServer.LOGGER.info("Done (" + s3 + ")! For help, type \"help\" or \"?\"");
            if (this.abstractPropertiesHandler.getBooleanOrDefault("enable-query", false)) {
                DedicatedServer.LOGGER.info("Starting GS4 status listener");
                this.queryResponseHandler = new QueryResponseHandler((DedicatedServer) (Object) this);
                this.queryResponseHandler.start();
            }

            if (this.abstractPropertiesHandler.getBooleanOrDefault("enable-rcon", false)) {
                DedicatedServer.LOGGER.info("Starting remote control listener");
                this.rconServer = new RconServer((DedicatedServer) (Object) this);
                this.rconServer.start();
//                this.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(); // CraftBukkit
            }

            // CraftBukkit start
            if (this.server.getBukkitSpawnRadius() > -1) {
                DedicatedServer.LOGGER.info("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
                this.abstractPropertiesHandler.properties.remove("spawn-protection");
                this.abstractPropertiesHandler.getIntOrDefault("spawn-protection", this.server.getBukkitSpawnRadius());
                this.server.removeBukkitSpawnRadius();
                this.abstractPropertiesHandler.save();
            }
            // CraftBukkit end

            if (org.spigotmc.SpigotConfig.lateBind) {
                try {
                    this.getNetworkIo().bind(InetAddress.getByName(this.getServerIp()), this.getServerPort());
                } catch (IOException ioexception) {
                    DedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
                    DedicatedServer.LOGGER.warn("The exception was: {}", new Object[]{ioexception.toString()});
                    DedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
                    cir.setReturnValue(false);
                    return;
                }
            }
            //Fukkit - remove code spigot disabled

            cir.setReturnValue(true);
        }
    }
}
