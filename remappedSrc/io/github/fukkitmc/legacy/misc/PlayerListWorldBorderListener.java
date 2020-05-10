package io.github.fukkitmc.legacy.misc;

import net.minecraft.network.packet.s2c.play.WorldBorderS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;

public class PlayerListWorldBorderListener implements WorldBorderListener {

    PlayerManager playerList;

    public PlayerListWorldBorderListener(PlayerManager list){
        this.playerList = list;
    }

    public void onSizeChange(WorldBorder worldborder, double d0) {
        playerList.sendToAll(new WorldBorderS2CPacket(worldborder, WorldBorderS2CPacket.Type.SET_SIZE));
    }

    public void onInterpolateSize(WorldBorder worldborder, double d0, double d1, long i) {
        playerList.sendToAll(new WorldBorderS2CPacket(worldborder, WorldBorderS2CPacket.Type.LERP_SIZE));
    }

    public void onCenterChanged(WorldBorder worldborder, double d0, double d1) {
        playerList.sendToAll(new WorldBorderS2CPacket(worldborder, WorldBorderS2CPacket.Type.SET_CENTER));
    }

    public void onWarningTimeChanged(WorldBorder worldborder, int i) {
        playerList.sendToAll(new WorldBorderS2CPacket(worldborder, WorldBorderS2CPacket.Type.SET_WARNING_TIME));
    }

    public void onWarningBlocksChanged(WorldBorder worldborder, int i) {
        playerList.sendToAll(new WorldBorderS2CPacket(worldborder, WorldBorderS2CPacket.Type.SET_WARNING_BLOCKS));
    }

    public void b(WorldBorder worldborder, double d0) {}

    public void c(WorldBorder worldborder, double d0) {}
}
