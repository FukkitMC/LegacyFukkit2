package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "main", at = @At("HEAD"))
    private static void mainMethod(String[] args, CallbackInfo ci) {
        Main.main(args);
    }

}
