package io.github.fukkitmc.legacy.mixins.fukkit;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.DedicatedPlayerManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedPlayerManager.class)
public abstract class DedicatedPlayerListMixin extends PlayerManager {

    public DedicatedPlayerListMixin(MinecraftServer minecraftServer) {
        super(minecraftServer);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(MinecraftDedicatedServer dedicatedServer, CallbackInfo ci){
    }

}
