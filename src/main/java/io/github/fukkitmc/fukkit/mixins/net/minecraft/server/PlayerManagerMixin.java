package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.craftbukkit.CraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(MinecraftServer server, CallbackInfo ci){
        ((PlayerManager)(Object)this).cserver = server.server = new CraftServer((DedicatedServer) server, ((PlayerManager)(Object)this));
        ((PlayerManager)(Object)this).cserver.consoleCommandSender = org.bukkit.craftbukkit.command.ColouredConsoleSender.getInstance();
//        server.reader.addCompleter(new org.bukkit.craftbukkit.command.ConsoleCommandCompleter(server.server));
    }

}
