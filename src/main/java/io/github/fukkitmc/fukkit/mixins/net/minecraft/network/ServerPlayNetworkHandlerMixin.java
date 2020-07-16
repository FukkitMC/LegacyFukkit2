package io.github.fukkitmc.fukkit.mixins.net.minecraft.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Shadow public static Logger LOGGER;

    /**
     * @author legacy fukkit
     * @reason read line 30
     */
    @Overwrite
    public void executeCommand(String s) {
        org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.startTiming(); // Spigot
        // CraftBukkit start - whole method
        LOGGER.info(this.player.getName().getString() + " issued server command: " + s);

        CraftPlayer player = (CraftPlayer) Bukkit.getPlayer(this.player.getUuid());

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
            return;
        }

        try {
            if (Bukkit.getServer().dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(ClientConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
            return;
        }
        org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
        // this.minecraftServer.getCommandHandler().a(this.player, s);
        // CraftBukkit end
    }

}
