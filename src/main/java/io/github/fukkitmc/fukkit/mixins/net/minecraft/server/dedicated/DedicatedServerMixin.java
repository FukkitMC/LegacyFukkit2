package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.dedicated;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.craftbukkit.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.Proxy;

@Environment(EnvType.SERVER)
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    @Shadow
    public abstract DedicatedPlayerManager getPlayerManager();

    public DedicatedServerMixin(File gameDir, Proxy proxy, File file) {
        super(gameDir, proxy, file);
    }

    @Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;isOnlineMode()Z", shift = At.Shift.BEFORE))
    public void spigotLoadPlugins(CallbackInfoReturnable<Boolean> cir) {
        // Fukkit start
        org.spigotmc.SpigotConfig.init((File) Main.options.valueOf("spigot-settings"));
        org.spigotmc.SpigotConfig.registerCommands();
        // Fukkit end
        this.playerManager = (new DedicatedPlayerManager((DedicatedServer) (Object) this)); // Fukkit
        server.loadPlugins();
        server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
        // Spigot End
    }

    @Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;setPlayerManager(Lnet/minecraft/server/PlayerManager;)V"))
    public void cancelPlayerManagerSetup(CallbackInfoReturnable<Boolean> cir) {
        if (getPlayerManager() == null) {
            this.setPlayerManager(new DedicatedPlayerManager((DedicatedServer) (Object) this)); // Fukkit
        }
    }
}
