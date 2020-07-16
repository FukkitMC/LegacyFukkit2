package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.class_1541;
import net.minecraft.class_1639;
import net.minecraft.container.Container;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeInstanceImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.map.MapIcon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EntityTracker;
import net.minecraft.world.level.LevelInfo;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.*;
import org.bukkit.Statistic.Type;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.CraftEffect;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<String>();
    private final Set<UUID> hiddenPlayers = new HashSet<UUID>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;

    public CraftPlayer(CraftServer server, ServerPlayerEntity entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOperator(getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().addToOperators(getProfile());
        } else {
            server.getHandle().removeFromOperators(getProfile());
        }

        perm.recalculatePermissions();
    }

    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    public InetSocketAddress getAddress() {
        if (getHandle().networkHandler == null) return null;

        SocketAddress addr = getHandle().networkHandler.connection.getAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        if (ignoreSneaking) {
            return 1.62D;
        } else {
            if (isSneaking()) {
                return 1.54D;
            } else {
                return 1.62D;
            }
        }
    }

    @Override
    public void sendRawMessage(String message) {
        if (getHandle().networkHandler == null) return;

        for (Text component : CraftChatMessage.fromString(message)) {
            getHandle().networkHandler.sendPacket(new ChatMessageS2CPacket(component));
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getDisplayName() {
        return getHandle().displayName;
    }

    @Override
    public void setDisplayName(final String name) {
        getHandle().displayName = name == null ? getName() : name;
    }

    @Override
    public String getPlayerListName() {
        return getHandle().listName == null ? getName() : CraftChatMessage.fromComponent(getHandle().listName);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = getName();
        }
        getHandle().listName = name.equals(getName()) ? null : CraftChatMessage.fromString(name)[0];
        for (ServerPlayerEntity player : (List<ServerPlayerEntity>)server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, getHandle()));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer) obj;
        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;

        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
        }

        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        org.spigotmc.AsyncCatcher.catchOp( "player kick"); // Spigot
        if (getHandle().networkHandler == null) return;

        getHandle().networkHandler.disconnect(message == null ? "" : message);
    }

    @Override
    public void setCompassTarget(Location loc) {
        if (getHandle().networkHandler == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().networkHandler.sendPacket(new PlayerSpawnPositionS2CPacket(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
    }

    @Override
    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    @Override
    public void chat(String msg) {
        if (getHandle().networkHandler == null) return;

        getHandle().networkHandler.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        if (getHandle().networkHandler == null) return;

        String instrumentName = null;
        switch (instrument) {
        case 0:
            instrumentName = "harp";
            break;
        case 1:
            instrumentName = "bd";
            break;
        case 2:
            instrumentName = "snare";
            break;
        case 3:
            instrumentName = "hat";
            break;
        case 4:
            instrumentName = "bassattack";
            break;
        }

        float f = (float) Math.pow(2.0D, (note - 12.0D) / 12.0D);
        getHandle().networkHandler.sendPacket(new PlaySoundIdS2CPacket("note."+instrumentName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        if (getHandle().networkHandler == null) return;

        String instrumentName = null;
        switch (instrument.ordinal()) {
            case 0:
                instrumentName = "harp";
                break;
            case 1:
                instrumentName = "bd";
                break;
            case 2:
                instrumentName = "snare";
                break;
            case 3:
                instrumentName = "hat";
                break;
            case 4:
                instrumentName = "bassattack";
                break;
        }
        float f = (float) Math.pow(2.0D, (note.getId() - 12.0D) / 12.0D);
        getHandle().networkHandler.sendPacket(new PlaySoundIdS2CPacket("note."+instrumentName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (sound == null) {
            return;
        }
        playSound(loc, CraftSound.getSound(sound), volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        if (loc == null || sound == null || getHandle().networkHandler == null) return;

        double x = loc.getBlockX() + 0.5;
        double y = loc.getBlockY() + 0.5;
        double z = loc.getBlockZ() + 0.5;

        PlaySoundIdS2CPacket packet = new PlaySoundIdS2CPacket(sound, x, y, z, volume, pitch);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        if (getHandle().networkHandler == null) return;

        spigot().playEffect(loc, effect, data, 0, 0, 0, 0, 1, 1, 64); // Spigot
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        if (data != null) {
            Validate.isTrue(data.getClass().isAssignableFrom(effect.getData()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue);
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        if (getHandle().networkHandler == null) return;

        BlockUpdateS2CPacket packet = new BlockUpdateS2CPacket(((CraftWorld) loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

        packet.field_6123 = CraftMagicNumbers.getBlock(material).stateFromData(data);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        if (getHandle().networkHandler == null) {
            return;
        }

        if (lines == null) {
            lines = new String[4];
        }

        Validate.notNull(loc, "Location can not be null");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }

        Text[] components = CraftSign.sanitizeLines(lines);

        getHandle().networkHandler.sendPacket(new class_1541(getHandle().world, new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), components));
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().networkHandler == null) return false;

        /*
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int cx = x >> 4;
        int cz = z >> 4;

        if (sx <= 0 || sy <= 0 || sz <= 0) {
            return false;
        }

        if ((x + sx - 1) >> 4 != cx || (z + sz - 1) >> 4 != cz || y < 0 || y + sy > 128) {
            return false;
        }

        if (data.length != (sx * sy * sz * 5) / 2) {
            return false;
        }

        Packet51MapChunk packet = new Packet51MapChunk(x, y, z, sx, sy, sz, data);

        getHandle().playerConnection.sendPacket(packet);

        return true;
        */

        throw new NotImplementedException("Chunk changes do not yet work"); // TODO: Chunk changes.
    }

    @Override
    public void sendMap(MapView map) {
        if (getHandle().networkHandler == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        Collection<MapIcon> icons = new ArrayList<MapIcon>();
        for (MapCursor cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new MapIcon(cursor.getRawType(), cursor.getX(), cursor.getY(), cursor.getDirection()));
            }
        }

        MapUpdateS2CPacket packet = new MapUpdateS2CPacket(map.getId(), map.getScale().getValue(), icons, data.buffer, 0, 0, 0, 0);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        ServerPlayerEntity entity = getHandle();

        if (getHealth() == 0 || entity.removed) {
            return false;
        }

        if (entity.networkHandler == null || entity.networkHandler.isDisconnected()) {
           return false;
        }

        if (entity.field_7391 != null) {
            return false;
        }

        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
        server.getPluginManager().callEvent(event);

        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled()) {
            return false;
        }

        // If this player is riding another entity, we must dismount before teleporting.
        entity.startRiding(null);

        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        ServerWorld fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        ServerWorld toWorld = ((CraftWorld) to.getWorld()).getHandle();

        // Close any foreign inventory
        if (getHandle().openedContainer != getHandle().playerContainer) {
            getHandle().closeContainer();
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.networkHandler.teleport(to);
        } else {
            server.getHandle().moveToWorld(entity, toWorld.dimension.getType(), true, to, true);
        }
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setSneaking(sneak);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    @Override
    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    @Override
    public void loadData() {
        server.getHandle().saveHandler.getPlayerData(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().saveHandler.savePlayerData(getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        getHandle().openContainer(getHandle().openedContainer);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().checkSleepStatus();
    }

    @Override
    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        if (achievement.hasParent() && !hasAchievement(achievement.getParent())) {
            awardAchievement(achievement.getParent());
        }
        getHandle().getStatHandler().method_6399(getHandle(), CraftStatistic.getNMSAchievement(achievement), 1);
        getHandle().getStatHandler().method_6353(getHandle());
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        for (Achievement achieve : Achievement.values()) {
            if (achieve.getParent() == achievement && hasAchievement(achieve)) {
                removeAchievement(achieve);
            }
        }
        getHandle().getStatHandler().method_6399(getHandle(), CraftStatistic.getNMSAchievement(achievement), 0);
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        return getHandle().getStatHandler().method_6396(CraftStatistic.getNMSAchievement(achievement));
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        incrementStatistic(statistic, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        decrementStatistic(statistic, 1);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        return getHandle().getStatHandler().method_6397(CraftStatistic.getNMSStatistic(statistic));
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, getStatistic(statistic) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, getStatistic(statistic) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        net.minecraft.stat.Stat nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        getHandle().getStatHandler().method_6399(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        incrementStatistic(statistic, material, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        decrementStatistic(statistic, material, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stat.Stat nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return getHandle().getStatHandler().method_6397(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, material, getStatistic(statistic, material) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, material, getStatistic(statistic, material) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stat.Stat nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        getHandle().getStatHandler().method_6399(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        incrementStatistic(statistic, entityType, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        decrementStatistic(statistic, entityType, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stat.Stat nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return getHandle().getStatHandler().method_6397(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, entityType, getStatistic(statistic, entityType) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, entityType, getStatistic(statistic, entityType) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stat.Stat nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        getHandle().getStatHandler().method_6399(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        getHandle().timeOffset = time;
        getHandle().relativeTime = relative;
    }

    @Override
    public long getPlayerTimeOffset() {
        return getHandle().timeOffset;
    }

    @Override
    public long getPlayerTime() {
        return getHandle().getPlayerTime();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return getHandle().relativeTime;
    }

    @Override
    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        getHandle().setPlayerWeather(type, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return getHandle().getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        getHandle().resetPlayerWeather();
    }

    @Override
    public boolean isBanned() {
        return server.getBanList(BanList.Type.NAME).isBanned(getName());
    }

    @Override
    public void setBanned(boolean value) {
        if (value) {
            server.getBanList(BanList.Type.NAME).addBan(getName(), null, null, null);
        } else {
            server.getBanList(BanList.Type.NAME).pardon(getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandle().getWhitelist().isAllowed(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().removeFromWhitelist(getProfile());
        } else {
            server.getHandle().addToWhitelist(getProfile());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (getHandle().networkHandler == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        if (mode != getGameMode()) {
            PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            
            getHandle().method_6073(getHandle());
            getHandle().interactionManager.setGameMode(LevelInfo.GameMode.byId(mode.getValue()));
            getHandle().fallDistance = 0;
            getHandle().networkHandler.sendPacket(new GameStateChangeS2CPacket(3, mode.getValue()));
        }
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().interactionManager.getGameMode().getId());
    }

    @Override
    public void giveExp(int exp) {
        getHandle().method_8049(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        getHandle().method_7973(levels);
    }

    @Override
    public float getExp() {
        return getHandle().experienceProgress;
    }

    @Override
    public void setExp(float exp) {
        getHandle().experienceProgress = exp;
        getHandle().field_6649 = -1;
    }

    @Override
    public int getLevel() {
        return getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        getHandle().experienceLevel = level;
        getHandle().field_6649 = -1;
    }

    @Override
    public int getTotalExperience() {
        return getHandle().totalExperience;
    }

    @Override
    public void setTotalExperience(int exp) {
        getHandle().totalExperience = exp;
    }

    @Override
    public float getExhaustion() {
        return getHandle().getHungerManager().exhaustion;
    }

    @Override
    public void setExhaustion(float value) {
        getHandle().getHungerManager().exhaustion = value;
    }

    @Override
    public float getSaturation() {
        return getHandle().getHungerManager().foodSaturationLevel;
    }

    @Override
    public void setSaturation(float value) {
        getHandle().getHungerManager().foodSaturationLevel = value;
    }

    @Override
    public int getFoodLevel() {
        return getHandle().getHungerManager().foodLevel;
    }

    @Override
    public void setFoodLevel(int value) {
        getHandle().getHungerManager().foodLevel = value;
    }

    @Override
    public Location getBedSpawnLocation() {
        World world = getServer().getWorld(getHandle().spawnWorld);
        BlockPos bed = getHandle().getSpawnPosition();

        if (world != null && bed != null) {
            bed = PlayerEntity.findRespawnPosition(((CraftWorld) world).getHandle(), bed, getHandle().isSpawnForced());
            if (bed != null) {
                return new Location(world, bed.getX(), bed.getY(), bed.getZ());
            }
        }
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        if (location == null) {
            getHandle().setPlayerSpawn(null, override);
        } else {
            getHandle().setPlayerSpawn(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            getHandle().spawnWorld = location.getWorld().getName();
        }
    }

    @Override
    public void hidePlayer(Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (getHandle().networkHandler == null) return;
        if (equals(player)) return;
        if (hiddenPlayers.contains(player.getUniqueId())) return;
        hiddenPlayers.add(player.getUniqueId());

        //remove this player from the hidden player's EntityTrackerEntry
        EntityTracker tracker = ((ServerWorld) entity.world).field_6621;
        ServerPlayerEntity other = ((CraftPlayer) player).getHandle();
        class_1639 entry = (class_1639) tracker.field_6589.get(other.getEntityId());
        if (entry != null) {
            entry.method_6109(getHandle());
        }

        //remove the hidden player from this player user list
        getHandle().networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, other));
    }

    @Override
    public void showPlayer(Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (getHandle().networkHandler == null) return;
        if (equals(player)) return;
        if (!hiddenPlayers.contains(player.getUniqueId())) return;
        hiddenPlayers.remove(player.getUniqueId());

        EntityTracker tracker = ((ServerWorld) entity.world).field_6621;
        ServerPlayerEntity other = ((CraftPlayer) player).getHandle();

        getHandle().networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, other));

        class_1639 entry = (class_1639) tracker.field_6589.get(other.getEntityId());
        if (entry != null && !entry.field_6688.contains(getHandle())) {
            entry.method_6106(getHandle());
        }
    }

    public void removeDisconnectingPlayer(Player player) {
        hiddenPlayers.remove(player.getUniqueId());
    }

    @Override
    public boolean canSee(Player player) {
        return !hiddenPlayers.contains(player.getUniqueId());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public ServerPlayerEntity getHandle() {
        return (ServerPlayerEntity) entity;
    }

    public void setHandle(final ServerPlayerEntity entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return hash;
    }

    @Override
    public long getFirstPlayed() {
        return firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(CompoundTag nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.contains("bukkit")) {
            CompoundTag data = nbttagcompound.getCompound("bukkit");

            if (data.contains("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }

            if (data.contains("newExp")) {
                ServerPlayerEntity handle = getHandle();
                handle.newExp = data.getInt("newExp");
                handle.newTotalExp = data.getInt("newTotalExp");
                handle.newLevel = data.getInt("newLevel");
                handle.expToDrop = data.getInt("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }

    public void setExtraData(CompoundTag nbttagcompound) {
        if (!nbttagcompound.contains("bukkit")) {
            nbttagcompound.put("bukkit", new CompoundTag());
        }

        CompoundTag data = nbttagcompound.getCompound("bukkit");
        ServerPlayerEntity handle = getHandle();
        data.putInt("newExp", handle.newExp);
        data.putInt("newTotalExp", handle.newTotalExp);
        data.putInt("newLevel", handle.newLevel);
        data.putInt("expToDrop", handle.expToDrop);
        data.putBoolean("keepLevel", handle.keepLevel);
        data.putLong("firstPlayed", getFirstPlayed());
        data.putLong("lastPlayed", System.currentTimeMillis());
        data.putString("lastKnownName", handle.getTranslationKey());
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandle().networkHandler == null) return;

        if (channels.contains(channel)) {
            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(channel, new PacketByteBuf(Unpooled.wrappedBuffer(message)));
            getHandle().networkHandler.sendPacket(packet);
        }
    }

    @Override
    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");

        getHandle().method_6068(url, "null");
    }

    public void addChannel(String channel) {
       com.google.common.base.Preconditions.checkState( channels.size() < 128, "Too many channels registered" ); // Spigot
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    public void sendSupportedChannels() {
        if (getHandle().networkHandler == null) return;
        Set<String> listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
                }
            }

            getHandle().networkHandler.sendPacket(new CustomPayloadS2CPacket("REGISTER", new PacketByteBuf(Unpooled.wrappedBuffer(stream.toByteArray()))));
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        Container container = getHandle().openedContainer;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        getHandle().onContainerPropertyUpdate(container, prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        conversationTracker.abandonAllConversations();
        perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return getHandle().abilities.flying;
    }

    @Override
    public void setFlying(boolean value) {
        if (!getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }

        getHandle().abilities.flying = value;
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().abilities.allowFlying;
    }

    @Override
    public void setAllowFlight(boolean value) {
        if (isFlying() && !value) {
            getHandle().abilities.flying = false;
        }

        getHandle().abilities.allowFlying = value;
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public int getNoDamageTicks() {
        if (getHandle().field_6650 > 0) {
            return Math.max(getHandle().field_6650, getHandle().field_7357);
        } else {
            return getHandle().field_7357;
        }
    }

    @Override
    public void setFlySpeed(float value) {
        validateSpeed(value);
        ServerPlayerEntity player = getHandle();
        player.abilities.flySpeed = Math.max( value, 0.0001f ) / 2f; // Spigot
        player.sendAbilitiesUpdate();

    }

    @Override
    public void setWalkSpeed(float value) {
        validateSpeed(value);
        ServerPlayerEntity player = getHandle();
        player.abilities.walkSpeed = Math.max( value, 0.0001f ) / 2f; // Spigot
        player.sendAbilitiesUpdate();
    }

    @Override
    public float getFlySpeed() {
        return getHandle().abilities.flySpeed * 2f;
    }

    @Override
    public float getWalkSpeed() {
        return getHandle().abilities.walkSpeed * 2f;
    }

    private void validateSpeed(float value) {
        if (value < 0) {
            if (value < -1f) {
                throw new IllegalArgumentException(value + " is too low");
            }
        } else {
            if (value > 1f) {
                throw new IllegalArgumentException(value + " is too high");
            }
        }
    }

    @Override
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, health);
        getHandle().markHealthDirty();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().markHealthDirty();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        ServerPlayNetworkHandler playerConnection = getHandle().networkHandler;
        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        }
        if (playerConnection.isDisconnected()) {
            // throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer"); // Spigot - remove this as Mojang's semi asynchronous Netty implementation can lead to races
        }

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0F, "Must be greater than 0");
        healthScale = value;
        scaledHealth = true;
        updateScaledHealth();
    }

    @Override
    public double getHealthScale() {
        return healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            updateScaledHealth();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return scaledHealth;
    }

    public float getScaledHealth() {
        return (float) (isHealthScaled() ? getHealth() * getHealthScale() / getMaxHealth() : getHealth());
    }

    @Override
    public double getHealth() {
        return health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        EntityAttributeContainer attributemapserver = (EntityAttributeContainer) getHandle().method_7147();
        Set set = attributemapserver.getTrackedAttributes();

        injectScaledMaxHealth(set, true);

        getHandle().getDataTracker().method_7259(6, (float) getScaledHealth());
        getHandle().networkHandler.sendPacket(new HealthUpdateS2CPacket(getScaledHealth(), getHandle().getHungerManager().getFoodLevel(), getHandle().getHungerManager().getSaturationLevel()));
        getHandle().networkHandler.sendPacket(new EntityAttributesS2CPacket(getHandle().getEntityId(), set));

        set.clear();
        getHandle().maxHealthCache = getMaxHealth();
    }

    public void injectScaledMaxHealth(Collection collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (Object genericInstance : collection) {
            EntityAttribute attribute = ((EntityAttributeInstance) genericInstance).getAttribute();
            if (attribute.getId().equals("generic.maxHealth")) {
                collection.remove(genericInstance);
                break;
            }
        }
        // Spigot start
        double healthMod = scaledHealth ? healthScale : getMaxHealth();
        if ( healthMod >= Float.MAX_VALUE || healthMod <= 0 )
        {
            healthMod = 20; // Reset health
            getServer().getLogger().warning( getName() + " tried to crash the server with a large health attribute" );
        }
        collection.add(new EntityAttributeInstanceImpl(getHandle().method_7147(), (new ClampedEntityAttribute(null, "generic.maxHealth", healthMod, 0.0D, Float.MAX_VALUE)).setName("Max Health").setTracked(true)));
        // Spigot end
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = getHandle().method_6061(); // PAIL
        return followed == getHandle() ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        getHandle().method_6073((entity == null) ? null : ((CraftEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        if (title != null) {
            TitleS2CPacket packetTitle = new TitleS2CPacket(Action.field_6343, CraftChatMessage.fromString(title)[0]);
            getHandle().networkHandler.sendPacket(packetTitle);
        }

        if (subtitle != null) {
            TitleS2CPacket packetSubtitle = new TitleS2CPacket(Action.field_6344, CraftChatMessage.fromString(subtitle)[0]);
            getHandle().networkHandler.sendPacket(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        TitleS2CPacket packetReset = new TitleS2CPacket(Action.field_6347, null);
        getHandle().networkHandler.sendPacket(packetReset);
    }

    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot()
    {

        @Override
        public InetSocketAddress getRawAddress()
        {
            return (InetSocketAddress) getHandle().networkHandler.connection.getRawAddress();
        }

        @Override
        public boolean getCollidesWithEntities()
        {
            return getHandle().collidesWithEntities;
        }

        @Override
        public void setCollidesWithEntities(boolean collides)
        {
            getHandle().collidesWithEntities = collides;
            getHandle().field_7390 = collides; // First boolean of Entity
        }

        @Override
        public void respawn()
        {
            if ( getHealth() <= 0 && isOnline() )
            {
                server.getServer().getPlayerList().respawnPlayer( getHandle(), 0, false );
            }
        }

        @Override
        public void playEffect( Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius )
        {
            Validate.notNull( location, "Location cannot be null" );
            Validate.notNull( effect, "Effect cannot be null" );
            Validate.notNull( location.getWorld(), "World cannot be null" );
            Packet packet;
            if ( effect.getType() != Effect.Type.PARTICLE )
            {
                int packetData = effect.getId();
                packet = new WorldEventS2CPacket( packetData, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ() ), id, false );
            } else
            {
                net.minecraft.client.particle.ParticleTypes particle = null;
                int[] extra = null;
                for ( net.minecraft.client.particle.ParticleTypes p : net.minecraft.client.particle.ParticleTypes.values() )
                {
                    if ( effect.getName().startsWith( p.getName().replace("_", "") ) )
                    {
                        particle = p;
                        if ( effect.getData() != null ) 
                        {
                            if ( effect.getData().equals( org.bukkit.Material.class ) )
                            {
                                extra = new int[]{ id };
                            } else 
                            {
                                extra = new int[]{ (data << 12) | (id & 0xFFF) };
                            }
                        }
                        break;
                    }
                }
                if ( extra == null )
                {
                    extra = new int[0];
                }
                packet = new ParticleS2CPacket( particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra );
            }
            int distance;
            radius *= radius;
            if ( getHandle().networkHandler == null )
            {
                return;
            }
            if ( !location.getWorld().equals( getWorld() ) )
            {
                return;
            }

            distance = (int) getLocation().distanceSquared( location );
            if ( distance <= radius )
            {
                getHandle().networkHandler.sendPacket( packet );
            }
        }

        @Override
        public String getLocale()
        {
           return getHandle().lang;
        }

        @Override
        public Set<Player> getHiddenPlayers()
        {
            Set<Player> ret = new HashSet<Player>();
            for ( UUID u : hiddenPlayers )
            {
                ret.add( getServer().getPlayer( u ) );
            }

            return java.util.Collections.unmodifiableSet( ret );
        }

        @Override
        public void sendMessage(BaseComponent component) {
          sendMessage( new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(BaseComponent... components) {
           if ( getHandle().networkHandler == null ) return;

            ChatMessageS2CPacket packet = new ChatMessageS2CPacket();
            packet.components = components;
            getHandle().networkHandler.sendPacket(packet);
        }
    };

    public Player.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
