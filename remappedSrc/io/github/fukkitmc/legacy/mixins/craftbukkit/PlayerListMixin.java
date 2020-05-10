package io.github.fukkitmc.legacy.mixins.craftbukkit;

import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.MinecraftServerExtra;
import io.github.fukkitmc.legacy.extra.PlayerListExtra;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.HeldItemChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.CommonI18n;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.level.LevelProperties;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
public abstract class PlayerListMixin {

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

    @Shadow public Map<UUID, ServerStatHandler> o;

    @Shadow public abstract int getPlayerCount();

    @Shadow public abstract void savePlayerFile(ServerPlayerEntity entityPlayer);

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(MinecraftServer minecraftServer, CallbackInfo ci){
        this.players = new java.util.concurrent.CopyOnWriteArrayList(); // CraftBukkit - ArrayList -> CopyOnWriteArrayList: Iterator safety
        ((PlayerManager)(Object)this).cserver = minecraftServer.server = new CraftServer(minecraftServer, ((PlayerManager)(Object)this));
        minecraftServer.console = org.bukkit.craftbukkit.command.ColouredConsoleSender.getInstance();
        minecraftServer.reader.addCompleter(new org.bukkit.craftbukkit.command.ConsoleCommandCompleter(minecraftServer.server));
    }


    /**
     * @author
     */
    @Overwrite
    public CompoundTag a(ServerPlayerEntity entityplayer) {
        CompoundTag nbttagcompound = this.server.worlds.get(0).getWorldData().i(); // CraftBukkit
        CompoundTag nbttagcompound1;
        if (entityplayer.getName().equals(this.server.getUserName()) && nbttagcompound != null) {
            entityplayer.fromTag(nbttagcompound);
            nbttagcompound1 = nbttagcompound;
            PlayerManager.LOGGER.debug("loading single player");
        } else {
            nbttagcompound1 = this.playerFileData.getPlayerData(entityplayer);
        }
        return nbttagcompound1;
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void a(ClientConnection networkmanager, ServerPlayerEntity entityplayer) {
        GameProfile gameprofile = entityplayer.getGameProfile();
        UserCache usercache = ((MinecraftServerExtra)this.server).getUserCache();
        GameProfile gameprofile1 = usercache.getByUuid(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();

        usercache.add(gameprofile);
        CompoundTag nbttagcompound = this.a(entityplayer);
        // CraftBukkit start - Better rename detection
        if (nbttagcompound != null && nbttagcompound.contains("bukkit")) {
            CompoundTag bukkit = nbttagcompound.getCompound("bukkit");
            s = bukkit.contains("lastKnownName", 8) ? bukkit.getString("lastKnownName") : s;
        }
        // CraftBukkit end

        entityplayer.setWorld(((MinecraftServerExtra)this.server).getWorldServer(entityplayer.dimension));
        entityplayer.playerInteractManager.a((ServerWorld) entityplayer.world);
        String s1 = "local";

        if (networkmanager.getAddress() != null) {
            s1 = networkmanager.getAddress().toString();
        }

        // CraftBukkit - Moved message to after join
        // PlayerList.f.info(entityplayer.getName() + "[" + s1 + "] logged in with entity id " + entityplayer.getId() + " at (" + entityplayer.locX + ", " + entityplayer.locY + ", " + entityplayer.locZ + ")");
        ServerWorld worldserver = ((MinecraftServerExtra)this.server).getWorldServer(entityplayer.dimension);
        LevelProperties worlddata = worldserver.getWorldData();
        BlockPos blockposition = worldserver.getSpawn();

        this.a(entityplayer, (ServerPlayerEntity) null, worldserver);
        ServerPlayNetworkHandler playerconnection = new ServerPlayNetworkHandler(this.server, networkmanager, entityplayer);

        playerconnection.sendPacket(new GameJoinS2CPacket(entityplayer.getEntityId(), entityplayer.playerInteractManager.getGameMode(), worlddata.isHardcore(), worldserver.dimension.getDimension(), worldserver.getDifficulty(), Math.min(this.getMaxPlayers(), 60), worlddata.getGeneratorType(), worldserver.getGameRules().getBoolean("reducedDebugInfo"))); // CraftBukkit - cap player list to 60
        ((CraftPlayer)((EntityExtra)entityplayer).getBukkitEntity()).sendSupportedChannels(); // CraftBukkit
        playerconnection.sendPacket(new CustomPayloadS2CPacket("MC|Brand", (new PacketByteBuf(Unpooled.buffer())).writeString(this.server.getServerModName())));
        playerconnection.sendPacket(new DifficultyS2CPacket(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
        playerconnection.sendPacket(new PlayerSpawnPositionS2CPacket(blockposition));
        playerconnection.sendPacket(new PlayerAbilitiesS2CPacket(entityplayer.abilities));
        playerconnection.sendPacket(new HeldItemChangeS2CPacket(entityplayer.inventory.selectedSlot));
        entityplayer.getStatisticManager().updateStatSet();
        entityplayer.getStatisticManager().updateStatistics(entityplayer);
        this.sendScoreboard((ServerScoreboard) worldserver.getScoreboard(), entityplayer);
        this.server.forcePlayerSampleUpdate();
        // CraftBukkit start - login message is handled in the event
        // ChatMessage chatmessage;

        String joinMessage;
        if (!entityplayer.getName().equalsIgnoreCase(s)) {
            // chatmessage = new ChatMessage("multiplayer.player.joined.renamed", new Object[] { entityplayer.getScoreboardDisplayName(), s});
            joinMessage = "\u00A7e" + CommonI18n.translate("multiplayer.player.joined.renamed", entityplayer.getName(), s);
        } else {
            // chatmessage = new ChatMessage("multiplayer.player.joined", new Object[] { entityplayer.getScoreboardDisplayName()});
            joinMessage = "\u00A7e" + CommonI18n.translate("multiplayer.player.joined", entityplayer.getName());
        }

        ((PlayerListExtra)this).onPlayerJoin(entityplayer, joinMessage);
        // CraftBukkit end
        worldserver = ((MinecraftServerExtra)this.server).getWorldServer(entityplayer.dimension);  // CraftBukkit - Update in case join event changed it
        playerconnection.requestTeleport(entityplayer.x, entityplayer.y, entityplayer.z, entityplayer.yaw, entityplayer.pitch);
        ((PlayerManager)(Object)this).b(entityplayer, worldserver);
        if (((MinecraftServerExtra)this.server).getResourcePack().length() > 0) {
            entityplayer.setResourcePack(((MinecraftServerExtra)this.server).getResourcePack(), ((MinecraftServerExtra)this.server).getResourcePackHash());
        }

        Iterator iterator = entityplayer.getEffects().iterator();

        while (iterator.hasNext()) {
            StatusEffectInstance mobeffect = (StatusEffectInstance) iterator.next();

            playerconnection.sendPacket(new EntityStatusEffectS2CPacket(entityplayer.getEntityId(), mobeffect));
        }

        entityplayer.syncInventory();
        if (nbttagcompound != null && nbttagcompound.contains("Riding", 10)) {
            Entity entity = EntityTypes.a(nbttagcompound.getCompound("Riding"), (World) worldserver);

            if (entity != null) {
                entity.attachedToPlayer = true;
                worldserver.spawnEntity(entity);
                entityplayer.mount(entity);
                entity.attachedToPlayer = false;
            }
        }

        // CraftBukkit - Moved from above, added world
        PlayerManager.LOGGER.info(entityplayer.getName() + "[" + s1 + "] logged in with entity id " + entityplayer.getEntityId() + " at ([" + entityplayer.world.levelProperties.getName() + "]" + entityplayer.x + ", " + entityplayer.y + ", " + entityplayer.z + ")");
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void disconnect(ServerPlayerEntity entityplayer) { // CraftBukkit - return string
        entityplayer.incrementStat(Stats.LEAVE_GAME);

        // CraftBukkit start - Quitting must be before we do final save of data, in case plugins need to modify it
        org.bukkit.craftbukkit.event.CraftEventFactory.handleInventoryCloseEvent(entityplayer);

        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.getName() + " left the game.");
        cserver.getPluginManager().callEvent(playerQuitEvent);
        ((CraftPlayer)((EntityExtra)entityplayer).getBukkitEntity()).disconnect(playerQuitEvent.getQuitMessage());
        // CraftBukkit end

        this.savePlayerFile(entityplayer);
        ServerWorld worldserver = entityplayer.u();

        if (entityplayer.vehicle != null && !(entityplayer.vehicle instanceof ServerPlayerEntity)) { // CraftBukkit - Don't remove players
            worldserver.removeEntity(entityplayer.vehicle);
            PlayerManager.LOGGER.debug("removing player mount");
        }

        worldserver.kill(entityplayer);
        worldserver.getPlayerChunkMap().removePlayer(entityplayer);
        this.players.remove(entityplayer);
        UUID uuid = entityplayer.getUuid();
        ServerPlayerEntity entityplayer1 = this.j.get(uuid);

        if (entityplayer1 == entityplayer) {
            this.j.remove(uuid);
            this.o.remove(uuid);
        }

        // CraftBukkit start
        //  this.sendAll(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { entityplayer}));
        PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, entityplayer);
        for (ServerPlayerEntity player : players) {
            ServerPlayerEntity entityplayer2 = (ServerPlayerEntity) player;

            if (((EntityExtra)entityplayer2).getBukkitEntity().canSee((CraftPlayer) ((EntityExtra)entityplayer).getBukkitEntity())) {
                entityplayer2.playerConnection.sendPacket(packet);
            } else {
                ((CraftPlayer) ((EntityExtra)entityplayer2).getBukkitEntity()).removeDisconnectingPlayer((Player) ((EntityExtra)entityplayer).getBukkitEntity());
            }
        }
        // This removes the scoreboard (and player reference) for the specific player in the manager
        cserver.getScoreboardManager().removePlayer((Player) ((EntityExtra)entityplayer).getBukkitEntity());
        // CraftBukkit end

        ChunkIOExecutor.adjustPoolSize(this.players.size()); // CraftBukkit

    }

}
