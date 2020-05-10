package io.github.fukkitmc.legacy.mixins.extra;


import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.legacy.debug.BytecodeAnchor;
import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.EntityPlayerExtra;
import io.github.fukkitmc.legacy.extra.MinecraftServerExtra;
import io.github.fukkitmc.legacy.extra.PlayerListExtra;
import io.github.fukkitmc.legacy.misc.PlayerListWorldBorderListener;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.border.WorldBorder;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerListMixin implements PlayerListExtra {

    @Shadow public MinecraftServer server;

    @Shadow public abstract void a(ServerPlayerEntity entityPlayer, ServerPlayerEntity entityPlayer2, World world);

    @Shadow public abstract void sendScoreboard(ServerScoreboard scoreboardServer, ServerPlayerEntity entityPlayer);

    @Shadow public abstract int getMaxPlayers();

    @Shadow public static Logger f;

    @Shadow public WorldSaveHandler playerFileData;

    @Shadow public List<ServerPlayerEntity> players;

    @Shadow public abstract void a(ServerPlayerEntity entityPlayer, ServerWorld worldServer);

    @Shadow public CraftServer cserver;

    @Shadow public Map<UUID, ServerPlayerEntity> j;

    @Override
    public ServerPlayerEntity moveToWorld(ServerPlayerEntity entityplayer, int i, boolean flag, Location location, boolean avoidSuffocation) {
        return null;
    }

    @Override
    public void setPlayerFileData(ServerWorld[] aworldserver) {
        if (playerFileData != null) return; // CraftBukkit
        this.playerFileData = aworldserver[0].getDataManager().getInstance();
        aworldserver[0].getWorldBorder().addListener(new PlayerListWorldBorderListener(((PlayerManager)(Object)this)));
    }

    @Override
    public void onPlayerJoin(ServerPlayerEntity entityplayer, String joinMessage) { // CraftBukkit added param
        this.players.add(entityplayer);
        this.j.put(entityplayer.getUuid(), entityplayer);
        // this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { entityplayer})); // CraftBukkit - replaced with loop below
        ServerWorld worldserver = ((MinecraftServerExtra)this.server).getWorldServer(entityplayer.dimension);

        // CraftBukkit start
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(cserver.getPlayer(entityplayer), joinMessage);
        cserver.getPluginManager().callEvent(playerJoinEvent);

        joinMessage = playerJoinEvent.getJoinMessage();

        if (joinMessage != null && joinMessage.length() > 0) {
            for (Text line : org.bukkit.craftbukkit.util.CraftChatMessage.fromString(joinMessage)) {
                ((MinecraftServerExtra)this.server).getPlayerList().sendToAll(new ChatMessageS2CPacket(line));
            }
        }

        ChunkIOExecutor.adjustPoolSize(this.players.size());
        // CraftBukkit end

        // CraftBukkit start - sendAll above replaced with this loop
        PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, entityplayer);

        for (ServerPlayerEntity player : this.players) {
            ServerPlayerEntity entityplayer1 = player;

            if (((EntityExtra)entityplayer1).getBukkitEntity().canSee((CraftPlayer) ((EntityExtra)entityplayer).getBukkitEntity())) {
                entityplayer1.playerConnection.sendPacket(packet);
            }

            if (!((EntityExtra)entityplayer).getBukkitEntity().canSee((CraftPlayer) ((EntityExtra)entityplayer1).getBukkitEntity())) {
                continue;
            }

            entityplayer.playerConnection.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, entityplayer1));
        }
        // CraftBukkit end

        // CraftBukkit start - Only add if the player wasn't moved in the event
        if (entityplayer.world == worldserver && !worldserver.players.contains(entityplayer)) {
            worldserver.spawnEntity(entityplayer);
            this.a(entityplayer, null);
        }
        // CraftBukkit end
    }

    /**
     * @author fukkit
     */
    @Overwrite(remap = false)
    public void b(ServerPlayerEntity entityplayer, ServerWorld worldserver) {
        WorldBorder worldborder = entityplayer.world.getWorldBorder(); // CraftBukkit

        entityplayer.playerConnection.sendPacket(new WorldBorderS2CPacket(worldborder, WorldBorderS2CPacket.Type.INITIALIZE));
        entityplayer.playerConnection.sendPacket(new WorldTimeUpdateS2CPacket(worldserver.getTime(), worldserver.getTimeOfDay(), worldserver.getGameRules().getBoolean("doDaylightCycle")));
        if (worldserver.isRaining()) {
            // CraftBukkit start - handle player weather
            ((EntityPlayerExtra)entityplayer).setPlayerWeather(org.bukkit.WeatherType.DOWNFALL, false);
            ((EntityPlayerExtra)entityplayer).updateWeather(-worldserver.rainGradient, worldserver.rainGradient, -worldserver.thunderGradient, worldserver.thunderGradient);
            // CraftBukkit end
        }

    }

    /**
     * @author fukkit
     */
    @Overwrite
    public String[] getSeenPlayers() {
        return this.server.worlds.get(0).getDataManager().getInstance().getSavedPlayerIds();
    }
}
