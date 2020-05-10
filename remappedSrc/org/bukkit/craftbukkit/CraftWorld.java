package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import io.github.fukkitmc.legacy.extra.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.Leaves1Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Log1Block;
import net.minecraft.block.PlanksBlock;
import net.minecraft.block.RedstoneComponentBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EndCrystalEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.LeadKnotEntity;
import net.minecraft.entity.decoration.PaintingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombiePigmanEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.thrown.EggEntity;
import net.minecraft.entity.thrown.EnderPearlEntity;
import net.minecraft.entity.thrown.ExperienceBottleEntity;
import net.minecraft.entity.thrown.EyeOfEnderEntity;
import net.minecraft.entity.thrown.PotionEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.vehicle.SpawnerMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.SlicedVoxelShape;
import net.minecraft.world.chunk.ServerChunkCache;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.storage.WorldLoadException;
import org.apache.commons.lang.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongObjectHashMap;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.util.Vector;

public class CraftWorld implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;

    private final ServerWorld world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    private final ChunkGenerator generator;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int ambientSpawn = -1;
    private int chunkLoadCount = 0;
    private int chunkGCTickCount;

    private static final Random rand = new Random();

    public CraftWorld(ServerWorld world, ChunkGenerator gen, Environment env) {
        this.world = world;
        this.generator = gen;

        environment = env;

        if (server.chunkGCPeriod > 0) {
            chunkGCTickCount = rand.nextInt(server.chunkGCPeriod);
        }
    }

    public Block getBlockAt(int x, int y, int z) {
        Chunk c = getChunkAt(x >> 4, z >> 4);
        return c.getBlock(x & 0xF, y, z & 0xF);
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return CraftMagicNumbers.getId(world.getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    public int getHighestBlockYAt(int x, int z) {
        if (!isChunkLoaded(x >> 4, z >> 4)) {
            loadChunk(x >> 4, z >> 4);
        }

        return world.getHighestBlockYAt(new BlockPos(x, 0, z)).getY();
    }

    public Location getSpawnLocation() {
        BlockPos spawn = world.getSpawn();
        return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = getSpawnLocation();
            world.levelProperties.setSpawn(new BlockPos(x, y, z));

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Chunk getChunkAt(int x, int z) {
        return this.world.chunkProviderServer.getChunkAt(x, z).bukkitChunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(int x, int z) {
        return world.chunkProviderServer.isChunkLoaded(x, z);
    }

    public Chunk[] getLoadedChunks() {
        Object[] chunks = world.chunkProviderServer.chunks.entries; //FIXME: fukkit:not a long object hashmap so this wont work
        org.bukkit.Chunk[] craftChunks = new CraftChunk[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            net.minecraft.world.chunk.Chunk chunk = (net.minecraft.world.chunk.Chunk) chunks[i];
            craftChunks[i] = chunk.bukkitChunk;
        }

        return craftChunks;
    }

    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk(x, z, save, false);
    }

    public boolean unloadChunkRequest(int x, int z) {
        return unloadChunkRequest(x, z, true);
    }

    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        if (safe && isChunkInUse(x, z)) {
            return false;
        }

        world.chunkProviderServer.queueUnload(x, z);

        return true;
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        if (safe && isChunkInUse(x, z)) {
            return false;
        }

        net.minecraft.world.chunk.Chunk chunk = world.chunkProviderServer.getChunk(x, z);
        if (chunk.mustSave) {   // If chunk had previously been queued to save, must do save to avoid loss of that data
            save = true;
        }

        chunk.removeEntities(); // Always remove entities - even if discarding, need to get them out of world table

        if (save && !(chunk instanceof EmptyChunk)) {
            world.chunkProviderServer.saveChunk(chunk);
            world.chunkProviderServer.saveChunkNOP(chunk);
        }

        world.chunkProviderServer.unloadQueue.remove(LongHash.toLong(x,z));
        world.chunkProviderServer.chunks.remove(LongHash.toLong(x, z));

        return true;
    }

    public boolean regenerateChunk(int x, int z) {
        unloadChunk(x, z, false, false);

        world.chunkProviderServer.unloadQueue.remove(LongHash.toLong(x,z));

        net.minecraft.world.chunk.Chunk chunk = null;

        if (world.chunkProviderServer.chunkProvider == null) {
            chunk = world.chunkProviderServer.emptyChunk;
        } else {
            chunk = world.chunkProviderServer.chunkProvider.getChunk(x, z);
        }

        chunkLoadPostProcess(chunk, x, z);

        refreshChunk(x, z);

        return chunk != null;
    }

    public boolean refreshChunk(int x, int z) {
        if (!isChunkLoaded(x, z)) {
            return false;
        }

        int px = x << 4;
        int pz = z << 4;

        // If there are more than 64 updates to a chunk at once, it will update all 'touched' sections within the chunk
        // And will include biome data if all sections have been 'touched'
        // This flags 65 blocks distributed across all the sections of the chunk, so that everything is sent, including biomes
        int height = getMaxHeight() / 16;
        for (int idx = 0; idx < 64; idx++) {
            world.notify(new BlockPos(px + (idx / height), ((idx % height) * 16), pz));
        }
        world.notify(new BlockPos(px + 15, (height * 16) - 1, pz + 15));

        return true;
    }

    public boolean isChunkInUse(int x, int z) {
        return ((PlayerChunkMapExtra)world.getPlayerChunkMap()).isChunkInUse(x, z);
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        chunkLoadCount++;
        if (generate) {
            // Use the default variant of loadChunk when generate == true.
            return world.chunkProviderServer.getChunkAt(x, z) != null;
        }

        world.chunkProviderServer.unloadQueue.remove(LongHash.toLong(x,z));
        net.minecraft.world.chunk.Chunk chunk = world.chunkProviderServer.chunks.get(LongHash.toLong(x, z));

        if (chunk == null) {
            chunk = world.chunkProviderServer.loadChunk(x, z);

            chunkLoadPostProcess(chunk, x, z);
        }
        return chunk != null;
    }

    private void chunkLoadPostProcess(net.minecraft.world.chunk.Chunk chunk, int cx, int cz) {
        if (chunk != null) {
            world.chunkProviderServer.chunks.set(LongHash.toLong(cx, cz), chunk);

            chunk.addEntities();

            // Update neighbor counts
            for (int x = -2; x < 3; x++) {
                for (int z = -2; z < 3; z++) {
                    if (x == 0 && z == 0) {
                        continue;
                    }

                    net.minecraft.world.chunk.Chunk neighbor = ((ChunkProviderServerExtra)world.chunkProviderServer).getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
                    if (neighbor != null) {
                        ((ChunkExtra)neighbor).setNeighborLoaded(-x, -z);
                        ((ChunkExtra)chunk).setNeighborLoaded(x, z);
                    }
                }
            }
            // CraftBukkit end

            chunk.loadNearby(world.chunkProviderServer, world.chunkProviderServer, cx, cz);
        }
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    public void loadChunk(Chunk chunk) {
        loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk) getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
    }

    public ServerWorld getHandle() {
        return world;
    }

    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item) {
        Validate.notNull(item, "Cannot drop a Null item.");
        Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
        ItemEntity entity = new ItemEntity(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.pickupDelay = 10;
        world.spawnEntity(entity);
        // TODO this is inconsistent with how Entity.getBukkitEntity() works.
        // However, this entity is not at the moment backed by a server entity class so it may be left.
        return new CraftItem(((WorldExtra)world).getServer(), entity);
    }

    private static void randomLocationWithinBlock(Location loc, double xs, double ys, double zs) {
        double prevX = loc.getX();
        double prevY = loc.getY();
        double prevZ = loc.getZ();
        loc.add(xs, ys, zs);
        if (loc.getX() < Math.floor(prevX)) {
            loc.setX(Math.floor(prevX));
        }
        if (loc.getX() >= Math.ceil(prevX)) {
            loc.setX(Math.ceil(prevX - 0.01));
        }
        if (loc.getY() < Math.floor(prevY)) {
            loc.setY(Math.floor(prevY));
        }
        if (loc.getY() >= Math.ceil(prevY)) {
            loc.setY(Math.ceil(prevY - 0.01));
        }
        if (loc.getZ() < Math.floor(prevZ)) {
            loc.setZ(Math.floor(prevZ));
        }
        if (loc.getZ() >= Math.ceil(prevZ)) {
            loc.setZ(Math.ceil(prevZ - 0.01));
        }
    }

    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        double xs = world.random.nextFloat() * 0.7F - 0.35D;
        double ys = world.random.nextFloat() * 0.7F - 0.35D;
        double zs = world.random.nextFloat() * 0.7F - 0.35D;
        loc = loc.clone();
        // Makes sure the new item is created within the block the location points to.
        // This prevents item spill in 1-block wide farms.
        randomLocationWithinBlock(loc, xs, ys, zs);
        return dropItem(loc, item);
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        Validate.notNull(loc, "Can not spawn arrow with a null location");
        Validate.notNull(velocity, "Can not spawn arrow with a null velocity");

        ArrowEntity arrow = new ArrowEntity(world);
        arrow.refreshPositionAndAngles(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        world.spawnEntity(arrow);
        return (Arrow) ((EntityExtra) arrow).getBukkitEntity();
    }

    @Deprecated
    public LivingEntity spawnCreature(Location loc, CreatureType creatureType) {
        return spawnCreature(loc, creatureType.toEntityType());
    }

    @Deprecated
    public LivingEntity spawnCreature(Location loc, EntityType creatureType) {
        Validate.isTrue(creatureType.isAlive(), "EntityType not instance of LivingEntity");
        return (LivingEntity) spawnEntity(loc, creatureType);
    }

    public Entity spawnEntity(Location loc, EntityType entityType) {
        return spawn(loc, entityType.getEntityClass());
    }

    public LightningStrike strikeLightning(Location loc) {
        LightningBoltEntity lightning = new LightningBoltEntity(world, loc.getX(), loc.getY(), loc.getZ());
        world.strikeLightning(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public LightningStrike strikeLightningEffect(Location loc) {
        LightningBoltEntity lightning = new LightningBoltEntity(world, loc.getX(), loc.getY(), loc.getZ());
        world.strikeLightning(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public boolean generateTree(Location loc, TreeType type) {
        net.minecraft.world.gen.feature.Feature gen;
        switch (type) {
        case BIG_TREE:
            gen = new WorldGenBigTree(true);
            break;
        case BIRCH:
            gen = new WorldGenForest(true, false);
            break;
        case REDWOOD:
            gen = new WorldGenTaiga2(true);
            break;
        case TALL_REDWOOD:
            gen = new WorldGenTaiga1();
            break;
        case JUNGLE:
            net.minecraft.block.BlockState iblockdata1 = Blocks.LOG.getDefaultState().with(Log1Block.VARIANT, PlanksBlock.WoodType.JUNGLE);
            net.minecraft.block.BlockState iblockdata2 = Blocks.LEAVES.getDefaultState().with(Leaves1Block.VARIANT, PlanksBlock.WoodType.JUNGLE).with(LeavesBlock.CHECK_DECAY, Boolean.valueOf(false));
            gen = new SlicedVoxelShape(true, 10, 20, iblockdata1, iblockdata2); // Magic values as in BlockSapling
            break;
        case SMALL_JUNGLE:
            iblockdata1 = Blocks.LOG.getDefaultState().with(Log1Block.VARIANT, PlanksBlock.WoodType.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getDefaultState().with(Leaves1Block.VARIANT, PlanksBlock.WoodType.JUNGLE).with(LeavesBlock.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, false);
            break;
        case COCOA_TREE:
            iblockdata1 = Blocks.LOG.getDefaultState().with(Log1Block.VARIANT, PlanksBlock.WoodType.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getDefaultState().with(Leaves1Block.VARIANT, PlanksBlock.WoodType.JUNGLE).with(LeavesBlock.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, true);
            break;
        case JUNGLE_BUSH:
            iblockdata1 = Blocks.LOG.getDefaultState().with(Log1Block.VARIANT, PlanksBlock.WoodType.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getDefaultState().with(Leaves1Block.VARIANT, PlanksBlock.WoodType.OAK).with(LeavesBlock.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenGroundBush(iblockdata1, iblockdata2);
            break;
        case RED_MUSHROOM:
            gen = new WorldGenHugeMushroom(Blocks.RED_MUSHROOM_BLOCK);
            break;
        case BROWN_MUSHROOM:
            gen = new WorldGenHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
            break;
        case SWAMP:
            gen = new WorldGenSwampTree();
            break;
        case ACACIA:
            gen = new WorldGenAcaciaTree(true);
            break;
        case DARK_OAK:
            gen = new WorldGenForestTree(true);
            break;
        case MEGA_REDWOOD:
            gen = new WorldGenMegaTree(false, rand.nextBoolean());
            break;
        case TALL_BIRCH:
            gen = new WorldGenForest(true, true);
            break;
        case TREE:
        default:
            gen = new WorldGenTrees(true);
            break;
        }

        return gen.generate(world, rand, new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        world.captureTreeGeneration = true;
        world.captureBlockStates = true;
        boolean grownTree = generateTree(loc, type);
        world.captureBlockStates = false;
        world.captureTreeGeneration = false;
        if (grownTree) { // Copy block data to delegate
            for (BlockState blockstate : world.capturedBlockStates) {
                int x = blockstate.getX();
                int y = blockstate.getY();
                int z = blockstate.getZ();
                BlockPos position = new BlockPos(x, y, z);
                net.minecraft.block.Block oldBlock = world.getBlockState(position).getBlock();
                int typeId = blockstate.getTypeId();
                int data = blockstate.getRawData();
                int flag = ((CraftBlockState)blockstate).getFlag();
                delegate.setTypeIdAndData(x, y, z, typeId, data);
                net.minecraft.block.Block newBlock = world.getBlockState(position).getBlock();
                ((WorldExtra)world).notifyAndUpdatePhysics(position, null, oldBlock, newBlock, flag);
            }
            world.capturedBlockStates.clear();
            return true;
        } else {
            world.capturedBlockStates.clear();
            return false;
        }
    }

    public BlockEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.getBlockEntity(new BlockPos(x, y, z));
    }

    public String getName() {
        return world.levelProperties.getName();
    }

    @Deprecated
    public long getId() {
        return world.levelProperties.getSeed();
    }

    public UUID getUID() {
        return ((IDataManagerExtra)world.getDataManager()).getUUID();
    }

    @Override
    public String toString() {
        return "CraftWorld{name=" + getName() + '}';
    }

    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) time += 24000;
        return time;
    }

    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) margin += 24000;
        setFullTime(getFullTime() + margin);
    }

    public long getFullTime() {
        return world.getTimeOfDay();
    }

    public void setFullTime(long time) {
        world.setTimeOfDay(time);

        // Forces the client to update to the new time immediately
        for (Player p : getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().playerConnection == null) continue;

            cp.getHandle().playerConnection.sendPacket(new WorldTimeUpdateS2CPacket(cp.getHandle().world.getTime(), ((EntityPlayerExtra)cp.getHandle()).getPlayerTime(), cp.getHandle().world.getGameRules().getBoolean("doDaylightCycle")));
        }
    }

    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return !world.createExplosion(null, x, y, z, power, setFire, breakBlocks).wasCanceled;
    }

    public boolean createExplosion(Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment env) {
        if (environment != env) {
            environment = env;
            world.dimension = Dimension.byDimension(environment.getId());
        }
    }

    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getBlockTypeIdAt(Location location) {
        return getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    public Biome getBiome(int x, int z) {
        return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPos(x, 0, z)));
    }

    public void setBiome(int x, int z, Biome bio) {
        net.minecraft.world.biome.Biome bb = CraftBlock.biomeToBiomeBase(bio);
        if (this.world.blockExists(new BlockPos(x, 0, z))) {
            net.minecraft.world.chunk.Chunk chunk = this.world.getChunk(new BlockPos(x, 0, z));

            if (chunk != null) {
                byte[] biomevals = chunk.getBiomeIndex();
                biomevals[((z & 0xF) << 4) | (x & 0xF)] = (byte)bb.id;
            }
        }
    }

    public double getTemperature(int x, int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).temperature;
    }

    public double getHumidity(int x, int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).downfall;
    }

    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object o : world.entityList) {
            if (o instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mcEnt = (net.minecraft.entity.Entity) o;
                Entity bukkitEntity = ((EntityExtra)mcEnt).getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null) {
                    list.add(bukkitEntity);
                }
            }
        }

        return list;
    }

    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<LivingEntity>();

        for (Object o : world.entityList) {
            if (o instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mcEnt = (net.minecraft.entity.Entity) o;
                Entity bukkitEntity = ((EntityExtra)mcEnt).getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity) {
                    list.add((LivingEntity) bukkitEntity);
                }
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>)getEntitiesByClasses(classes);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        for (Object entity: world.entityList) {
            if (entity instanceof net.minecraft.entity.Entity) {
                Entity bukkitEntity = ((EntityExtra) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                if (clazz.isAssignableFrom(bukkitClass)) {
                    list.add((T) bukkitEntity);
                }
            }
        }

        return list;
    }

    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<Entity>();

        for (Object entity: world.entityList) {
            if (entity instanceof net.minecraft.entity.Entity) {
                Entity bukkitEntity = ((EntityExtra) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                for (Class<?> clazz : classes) {
                    if (clazz.isAssignableFrom(bukkitClass)) {
                        list.add(bukkitEntity);
                        break;
                    }
                }
            }
        }

        return list;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        if (location == null || !location.getWorld().equals(this)) {
            return Collections.emptyList();
        }

        Box bb = new Box(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
        List<net.minecraft.entity.Entity> entityList = getHandle().getEntitiesIn((net.minecraft.entity.Entity) null, bb, null); // PAIL : rename
        List<Entity> bukkitEntityList = new ArrayList<org.bukkit.entity.Entity>(entityList.size());
        for (Object entity : entityList) {
            bukkitEntityList.add(((EntityExtra) entity).getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(world.players.size());

        for (PlayerEntity human : world.players) {
            HumanEntity bukkitEntity = (HumanEntity) ((EntityExtra) human).getBukkitEntity();

            if ((bukkitEntity instanceof Player)) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    public void save() {
        this.server.checkSaveState();
        try {
            boolean oldSave = world.savingDisabled;

            world.savingDisabled = false;
            world.save(true, null);

            world.savingDisabled = oldSave;
        } catch (WorldLoadException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isAutoSave() {
        return !world.savingDisabled;
    }

    public void setAutoSave(boolean value) {
        world.savingDisabled = !value;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().levelProperties.setDifficulty(net.minecraft.world.Difficulty.byOrdinal(difficulty.getValue()));
    }

    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    public BlockMetadataStore getBlockMetadata() {
        return blockMetadata;
    }

    public boolean hasStorm() {
        return world.levelProperties.hasStorm();
    }

    public void setStorm(boolean hasStorm) {
        world.levelProperties.setStorm(hasStorm);
    }

    public int getWeatherDuration() {
        return world.levelProperties.getWeatherDuration();
    }

    public void setWeatherDuration(int duration) {
        world.levelProperties.setWeatherDuration(duration);
    }

    public boolean isThundering() {
        return world.levelProperties.isThundering();
    }

    public void setThundering(boolean thundering) {
        world.levelProperties.setThundering(thundering);
    }

    public int getThunderDuration() {
        return world.levelProperties.getThunderDuration();
    }

    public void setThunderDuration(int duration) {
        world.levelProperties.setThunderDuration(duration);
    }

    public long getSeed() {
        return world.levelProperties.getSeed();
    }

    public boolean getPVP() {
        return world.pvpMode;
    }

    public void setPVP(boolean pvp) {
        world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        playEffect(player.getLocation(), effect, data, 0);
    }

    public void playEffect(Location location, Effect effect, int data) {
        playEffect(location, effect, data, 64);
    }

    public <T> void playEffect(Location loc, Effect effect, T data) {
        playEffect(loc, effect, data, 64);
    }

    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Validate.isTrue(data.getClass().isAssignableFrom(effect.getData()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue, radius);
    }

    public void playEffect(Location location, Effect effect, int data, int radius) {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(effect, "Effect cannot be null");
        Validate.notNull(location.getWorld(), "World cannot be null");
        int packetData = effect.getId();
        WorldEventS2CPacket packet = new WorldEventS2CPacket(packetData, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), data, false);
        int distance;
        radius *= radius;

        for (Player player : getPlayers()) {
            if (((CraftPlayer) player).getHandle().playerConnection == null) continue;
            if (!location.getWorld().equals(player.getWorld())) continue;

            distance = (int) player.getLocation().distanceSquared(location);
            if (distance <= radius) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, SpawnReason.CUSTOM);
    }

    public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");

        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY() + 0.5;
        double z = location.getBlockZ() + 0.5;

        FallingBlockEntity entity = new FallingBlockEntity(world, x, y, z, net.minecraft.block.Block.byId(material.getId()).stateFromData(data));
        entity.time = 1;

        ((WorldExtra)world).addEntity(entity, SpawnReason.CUSTOM);
        return (FallingBlock) ((EntityExtra)entity).getBukkitEntity();
    }

    public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData) throws IllegalArgumentException {
        return spawnFallingBlock(location, org.bukkit.Material.getMaterial(blockId), blockData);
    }

    @SuppressWarnings("unchecked")
    public net.minecraft.entity.Entity createEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }

        net.minecraft.entity.Entity entity = null;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        // order is important for some of these
        if (Boat.class.isAssignableFrom(clazz)) {
            entity = new BoatEntity(world, x, y, z);
        } else if (FallingBlock.class.isAssignableFrom(clazz)) {
            x = location.getBlockX();
            y = location.getBlockY();
            z = location.getBlockZ();
            net.minecraft.block.BlockState blockData = world.getBlockState(new BlockPos(x, y, z));
            int type = CraftMagicNumbers.getId(blockData.getBlock());
            int data = blockData.getBlock().getData(blockData);

            entity = new FallingBlockEntity(world, x + 0.5, y + 0.5, z + 0.5, net.minecraft.block.Block.byId(type).stateFromData(data));
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new SnowballEntity(world, x, y, z);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EggEntity(world, x, y, z);
            } else if (Arrow.class.isAssignableFrom(clazz)) {
                entity = new ArrowEntity(world);
                entity.refreshPositionAndAngles(x, y, z, 0, 0);
            } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = new ExperienceBottleEntity(world);
                entity.refreshPositionAndAngles(x, y, z, 0, 0);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = new EnderPearlEntity(world, null);
                entity.refreshPositionAndAngles(x, y, z, 0, 0);
            } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                entity = new PotionEntity(world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.POTION, 1)));
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = new SmallFireballEntity(world);
                } else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = new WitherSkullEntity(world);
                } else {
                    entity = new FireballEntity(world);
                }
                entity.refreshPositionAndAngles(x, y, z, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((EntityFireballExtra) entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new FurnaceMinecartEntity(world, x, y, z);
            } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new ChestMinecartEntity(world, x, y, z);
            } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = new TntMinecartEntity(world, x, y, z);
            } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = new HopperMinecartEntity(world, x, y, z);
            } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = new SpawnerMinecartEntity(world, x, y, z);
            } else { // Default to rideable minecart for pre-rideable compatibility
                entity = new MinecartEntity(world, x, y, z);
            }
        } else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new EyeOfEnderEntity(world, x, y, z);
        } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = new EndCrystalEntity(world);
            entity.refreshPositionAndAngles(x, y, z, 0, 0);
        } else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = new ChickenEntity(world);
            } else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = new MooshroomEntity(world);
                } else {
                    entity = new CowEntity(world);
                }
            } else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = new SnowGolemEntity(world);
                } else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = new IronGolemEntity(world);
                }
            } else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = new CreeperEntity(world);
            } else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = new GhastEntity(world);
            } else if (Pig.class.isAssignableFrom(clazz)) {
                entity = new PigEntity(world);
            } else if (Player.class.isAssignableFrom(clazz)) {
                // need a net server handler for this one
            } else if (Sheep.class.isAssignableFrom(clazz)) {
                entity = new SheepEntity(world);
            } else if (Horse.class.isAssignableFrom(clazz)) {
                entity = new HorseBaseEntity(world);
            } else if (Skeleton.class.isAssignableFrom(clazz)) {
                entity = new AbstractSkeletonEntity(world);
            } else if (Slime.class.isAssignableFrom(clazz)) {
                if (MagmaCube.class.isAssignableFrom(clazz)) {
                    entity = new MagmaCubeEntity(world);
                } else {
                    entity = new SlimeEntity(world);
                }
            } else if (Spider.class.isAssignableFrom(clazz)) {
                if (CaveSpider.class.isAssignableFrom(clazz)) {
                    entity = new CaveSpiderEntity(world);
                } else {
                    entity = new SpiderEntity(world);
                }
            } else if (Squid.class.isAssignableFrom(clazz)) {
                entity = new SquidEntity(world);
            } else if (Tameable.class.isAssignableFrom(clazz)) {
                if (Wolf.class.isAssignableFrom(clazz)) {
                    entity = new WolfEntity(world);
                } else if (Ocelot.class.isAssignableFrom(clazz)) {
                    entity = new OcelotEntity(world);
                }
            } else if (PigZombie.class.isAssignableFrom(clazz)) {
                entity = new ZombiePigmanEntity(world);
            } else if (Zombie.class.isAssignableFrom(clazz)) {
                entity = new ZombieEntity(world);
            } else if (Giant.class.isAssignableFrom(clazz)) {
                entity = new GiantEntity(world);
            } else if (Silverfish.class.isAssignableFrom(clazz)) {
                entity = new SilverfishEntity(world);
            } else if (Enderman.class.isAssignableFrom(clazz)) {
                entity = new EndermanEntity(world);
            } else if (Blaze.class.isAssignableFrom(clazz)) {
                entity = new BlazeEntity(world);
            } else if (Villager.class.isAssignableFrom(clazz)) {
                entity = new VillagerEntity(world);
            } else if (Witch.class.isAssignableFrom(clazz)) {
                entity = new WitchEntity(world);
            } else if (Wither.class.isAssignableFrom(clazz)) {
                entity = new WitherEntity(world);
            } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                if (EnderDragon.class.isAssignableFrom(clazz)) {
                    entity = new EnderDragonEntity(world);
                }
            } else if (Ambient.class.isAssignableFrom(clazz)) {
                if (Bat.class.isAssignableFrom(clazz)) {
                    entity = new BatEntity(world);
                }
            } else if (Rabbit.class.isAssignableFrom(clazz)) {
                entity = new RabbitEntity(world);
            } else if (Endermite.class.isAssignableFrom(clazz)) {
                entity = new EndermiteEntity(world);
            } else if (Guardian.class.isAssignableFrom(clazz)){
                entity = new GuardianEntity(world);
            } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                entity = new ArmorStandEntity(world, x, y, z);
            }

            if (entity != null) {
                entity.updatePositionAndAngles(x, y, z, yaw, pitch);
            }
        } else if (Hanging.class.isAssignableFrom(clazz)) {
            Block block = getBlockAt(location);
            BlockFace face = BlockFace.SELF;

            int width = 16; // 1 full block, also painting smallest size.
            int height = 16; // 1 full block, also painting smallest size.

            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
            } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                width = 9;
                height = 9;
            }

            BlockFace[] faces = new BlockFace[]{BlockFace.EAST,BlockFace.NORTH,BlockFace.WEST,BlockFace.SOUTH};
            final BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
            for (BlockFace dir : faces) {
                net.minecraft.block.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
                if (nmsBlock.getMaterial().isBuildable() || RedstoneComponentBlock.d(nmsBlock)) {
                    boolean taken = false;
                    Box bb = DecorationEntity.calculateBoundingBox(pos,CraftBlock.blockFaceToNotch(dir).getOpposite(),width,height);
                    List<net.minecraft.entity.Entity> list = world.getEntitiesIn(null, bb);
                    for (Iterator<net.minecraft.entity.Entity> it = list.iterator(); !taken && it.hasNext();) {
                        net.minecraft.entity.Entity e = it.next();
                        if (e instanceof DecorationEntity) {
                            taken = true; // Hanging entities do not like hanging entities which intersect them.
                        }
                    }

                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }

            Direction dir = CraftBlock.blockFaceToNotch(face).getOpposite();
            
            if (Painting.class.isAssignableFrom(clazz)) {
                entity = new PaintingEntity(world, new BlockPos((int) x, (int) y, (int) z), dir);
            } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                entity = new ItemFrameEntity(world, new BlockPos((int) x, (int) y, (int) z), dir);
            } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                entity = new LeadKnotEntity(world, new BlockPos((int) x, (int) y, (int) z));
                entity.attachedToPlayer = true;
            }

            if (entity != null && !((DecorationEntity) entity).survives()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new TntEntity(world, x, y, z, null);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new ExperienceOrbEntity(world, x, y, z, 0);
        } else if (Weather.class.isAssignableFrom(clazz)) {
            // not sure what this can do
            if (LightningStrike.class.isAssignableFrom(clazz)) {
                entity = new LightningBoltEntity(world, x, y, z);
                // what is this, I don't even
            }
        } else if (Firework.class.isAssignableFrom(clazz)) {
            entity = new FireworkEntity(world, x, y, z, null);
        }

        if (entity != null) {
            return entity;
        }

        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.entity.Entity entity, SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        if (entity instanceof MobEntity) {
            ((MobEntity) entity).prepare(getHandle().E(new BlockPos(entity)), null);
        }

        ((WorldExtra)world).addEntity(entity, reason);
        return (T) ((EntityExtra)entity).getBukkitEntity();
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, SpawnReason reason) throws IllegalArgumentException {
        net.minecraft.entity.Entity entity = createEntity(location, clazz);

        return addEntity(entity, reason);
    }

    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setSpawnFlags(allowMonsters, allowAnimals);
    }

    public boolean getAllowAnimals() {
        return world.allowAnimals;
    }

    public boolean getAllowMonsters() {
        return world.allowMonsters;
    }

    public int getMaxHeight() {
        return world.getHeight();
    }

    public int getSeaLevel() {
        return 64;
    }

    public boolean getKeepSpawnInMemory() {
        return world.keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepLoaded) {
        world.keepSpawnInMemory = keepLoaded;
        // Grab the worlds spawn chunk
        BlockPos chunkcoordinates = this.world.getSpawn();
        int chunkCoordX = chunkcoordinates.getX() >> 4;
        int chunkCoordZ = chunkcoordinates.getZ() >> 4;
        // Cycle through the 25x25 Chunks around it to load/unload the chunks.
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                if (keepLoaded) {
                    loadChunk(chunkCoordX + x, chunkCoordZ + z);
                } else {
                    if (isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                        if (this.getHandle().getChunk(chunkCoordX + x, chunkCoordZ + z) instanceof EmptyChunk) {
                            unloadChunk(chunkCoordX + x, chunkCoordZ + z, false);
                        } else {
                            unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return getUID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CraftWorld other = (CraftWorld) obj;

        return this.getUID() == other.getUID();
    }

    public File getWorldFolder() {
        return world.getDataManager().getWorldFolder();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        for (Player player : getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    public org.bukkit.WorldType getWorldType() {
        return org.bukkit.WorldType.getByName(world.getWorldData().getGeneratorType().getName());
    }

    public boolean canGenerateStructures() {
        return world.getWorldData().hasStructures();
    }

    public long getTicksPerAnimalSpawns() {
        return world.ticksPerAnimalSpawns;
    }

    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }

    public long getTicksPerMonsterSpawns() {
        return world.ticksPerMonsterSpawns;
    }

    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public int getMonsterSpawnLimit() {
        if (monsterSpawn < 0) {
            return server.getMonsterSpawnLimit();
        }

        return monsterSpawn;
    }

    public void setMonsterSpawnLimit(int limit) {
        monsterSpawn = limit;
    }

    public int getAnimalSpawnLimit() {
        if (animalSpawn < 0) {
            return server.getAnimalSpawnLimit();
        }

        return animalSpawn;
    }

    public void setAnimalSpawnLimit(int limit) {
        animalSpawn = limit;
    }

    public int getWaterAnimalSpawnLimit() {
        if (waterAnimalSpawn < 0) {
            return server.getWaterAnimalSpawnLimit();
        }

        return waterAnimalSpawn;
    }

    public void setWaterAnimalSpawnLimit(int limit) {
        waterAnimalSpawn = limit;
    }

    public int getAmbientSpawnLimit() {
        if (ambientSpawn < 0) {
            return server.getAmbientSpawnLimit();
        }

        return ambientSpawn;
    }

    public void setAmbientSpawnLimit(int limit) {
        ambientSpawn = limit;
    }


    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (loc == null || sound == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        getHandle().makeSound(x, y, z, CraftSound.getSound(sound), volume, pitch);
    }

    public String getGameRuleValue(String rule) {
        return getHandle().getGameRules().getString(rule);
    }

    public boolean setGameRuleValue(String rule, String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!isGameRule(rule)) return false;

        getHandle().getGameRules().set(rule, value);
        return true;
    }

    public String[] getGameRules() {
        return getHandle().getGameRules().getGameRules();
    }

    public boolean isGameRule(String rule) {
        return getHandle().getGameRules().contains(rule);
    }

    @Override
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new CraftWorldBorder(this);
        }

        return this.worldBorder;
    }

    public void processChunkGC() {
        chunkGCTickCount++;

        if (chunkLoadCount >= server.chunkGCLoadThresh && server.chunkGCLoadThresh > 0) {
            chunkLoadCount = 0;
        } else if (chunkGCTickCount >= server.chunkGCPeriod && server.chunkGCPeriod > 0) {
            chunkGCTickCount = 0;
        } else {
            return;
        }
        ServerChunkCache cps = world.chunkProviderServer;
        LongObjectHashMap<net.minecraft.world.chunk.Chunk> chunks = (LongObjectHashMap)cps.chunks;
        //TODO: fukkit: a less cursed way would be better
        for (net.minecraft.world.chunk.Chunk chunk : chunks.values()) {
            // If in use, skip it
            if (isChunkInUse(chunk.locX, chunk.locZ)) {
                continue;
            }

            // Already unloading?
            if (cps.unloadQueue.contains(LongHash.toLong(chunk.locX, chunk.locZ))) {
                continue;
            }

            // Add unload request
            cps.queueUnload(chunk.locX, chunk.locZ);
        }
    }
}
