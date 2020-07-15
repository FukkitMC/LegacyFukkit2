package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.PlayerManager}
 */
public interface PlayerManagerExtra {

    void sendAll(net.minecraft.network.Packet var0, net.minecraft.world.World var1);

    void sendAll(net.minecraft.network.Packet var0, net.minecraft.entity.player.PlayerEntity var1);

    void repositionEntity(net.minecraft.entity.Entity var0, org.bukkit.Location var1, boolean var2);

    net.minecraft.entity.player.ServerPlayerEntity moveToWorld(net.minecraft.entity.player.ServerPlayerEntity var0, int var1, boolean var2, org.bukkit.Location var3, boolean var4);

    void onPlayerJoin(net.minecraft.entity.player.ServerPlayerEntity var0, java.lang.String var1);

    java.lang.String disconnect(net.minecraft.entity.player.ServerPlayerEntity var0);

    net.minecraft.entity.player.ServerPlayerEntity processLogin(com.mojang.authlib.GameProfile var0, net.minecraft.entity.player.ServerPlayerEntity var1);

    org.bukkit.Location calculateTarget(org.bukkit.Location var0, net.minecraft.world.World var1);

    void sendMessage(net.minecraft.text.Text[] var0);

    void changeDimension(net.minecraft.entity.player.ServerPlayerEntity var0, int var1, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var2);

    net.minecraft.entity.player.ServerPlayerEntity attemptLogin(net.minecraft.server.network.ServerLoginNetworkHandler var0, com.mojang.authlib.GameProfile var1, java.lang.String var2);
}
