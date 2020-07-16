package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.dedicated;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.craftbukkit.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.Proxy;

@Environment(EnvType.SERVER)
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    public DedicatedServerMixin(File gameDir, Proxy proxy, File file) {
        super(gameDir, proxy, file);
    }

    @Inject(method = "setupServer", at = @At("TAIL"))
    public void initSpigotConfig(CallbackInfoReturnable<Boolean> cir){
        // Fukkit start
        org.spigotmc.SpigotConfig.init((File) Main.options.valueOf("spigot-settings"));
        org.spigotmc.SpigotConfig.registerCommands();
        // Fukkit end
    }

}
