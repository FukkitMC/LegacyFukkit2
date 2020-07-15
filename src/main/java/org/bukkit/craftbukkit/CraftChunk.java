package org.bukkit.craftbukkit;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.BlockStorage;
import net.minecraft.world.chunk.SubChunk;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.ChunkSnapshot;

public class CraftChunk implements Chunk {
    private WeakReference<net.minecraft.world.chunk.Chunk> weakChunk;
    private final ServerWorld worldServer;
    private final int x;
    private final int z;
    private static final byte[] emptyData = new byte[2048];
    private static final short[] emptyBlockIDs = new short[4096];
    private static final byte[] emptySkyLight = new byte[2048];

    public CraftChunk(net.minecraft.world.chunk.Chunk chunk) {
        if (!(chunk instanceof SubChunk)) {
            this.weakChunk = new WeakReference<net.minecraft.world.chunk.Chunk>(chunk);
        }

        worldServer = (ServerWorld) getHandle().world;
        x = getHandle().field_1562;
        z = getHandle().field_1563;
    }

    public World getWorld() {
        return worldServer.getCraftWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    public net.minecraft.world.chunk.Chunk getHandle() {
        net.minecraft.world.chunk.Chunk c = weakChunk.get();

        if (c == null) {
            c = worldServer.getChunk(x, z);

            if (!(c instanceof SubChunk)) {
                weakChunk = new WeakReference<net.minecraft.world.chunk.Chunk>(c);
            }
        }

        return c;
    }

    void breakLink() {
        weakChunk.clear();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    public Block getBlock(int x, int y, int z) {
        return new CraftBlock(this, (getX() << 4) | (x & 0xF), y, (getZ() << 4) | (z & 0xF));
    }

    public Entity[] getEntities() {
        int count = 0, index = 0;
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        for (int i = 0; i < 16; i++) {
            count += chunk.entitySlices[i].size();
        }

        Entity[] entities = new Entity[count];

        for (int i = 0; i < 16; i++) {

            for (Object obj : chunk.entitySlices[i].toArray()) {
                if (!(obj instanceof net.minecraft.entity.Entity)) {
                    continue;
                }

                entities[index++] = ((net.minecraft.entity.Entity) obj).getBukkitEntity();
            }
        }

        return entities;
    }

    public BlockState[] getTileEntities() {
        int index = 0;
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        BlockState[] entities = new BlockState[chunk.blockEntities.size()];

        for (Object obj : chunk.blockEntities.keySet().toArray()) {
            if (!(obj instanceof BlockPos)) {
                continue;
            }

            BlockPos position = (BlockPos) obj;
            entities[index++] = worldServer.getCraftWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    public boolean unload(boolean save, boolean safe) {
        return getWorld().unloadChunk(getX(), getZ(), save, safe);
    }

    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        BlockStorage[] cs = chunk.method_1398();
        short[][] sectionBlockIDs = new short[cs.length][];
        byte[][] sectionBlockData = new byte[cs.length][];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];

        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == null) { // Section is empty?
                sectionBlockIDs[i] = emptyBlockIDs;
                sectionBlockData[i] = emptyData;
                sectionSkyLights[i] = emptySkyLight;
                sectionEmitLights[i] = emptyData;
                sectionEmpty[i] = true;
            } else { // Not empty
                short[] blockids = new short[4096];
                char[] baseids = cs[i].method_1436();
                byte[] dataValues = sectionBlockData[i] = new byte[2048];

                // Copy base IDs
                for (int j = 0; j < 4096; j++) {
                    if (baseids[j] == 0) continue;
                    net.minecraft.block.BlockState blockData = (net.minecraft.block.BlockState) net.minecraft.block.Block.BLOCK_STATES.fromId(baseids[j]);
                    if (blockData == null) continue;
                    blockids[j] = (short) net.minecraft.block.Block.getIdByBlock(blockData.getBlock());
                    int data = blockData.getBlock().getData(blockData);
                    int jj = j >> 1;
                    if ((j & 1) == 0) {
                        dataValues[jj] = (byte) ((dataValues[jj] & 0xF0) | (data & 0xF));
                    } else {
                        dataValues[jj] = (byte) ((dataValues[jj] & 0xF) | ((data & 0xF) << 4));
                    }
                }               

                sectionBlockIDs[i] = blockids;
                
                if (cs[i].method_1438() == null) {
                    sectionSkyLights[i] = emptyData;
                } else {
                    sectionSkyLights[i] = new byte[2048];
                    System.arraycopy(cs[i].method_1438().getValue(), 0, sectionSkyLights[i], 0, 2048);
                }
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(cs[i].getMetadata().getValue(), 0, sectionEmitLights[i], 0, 2048);
            }
        }

        int[] hmap = null;

        if (includeMaxBlockY) {
            hmap = new int[256]; // Get copy of height map
            System.arraycopy(chunk.heightmap, 0, hmap, 0, 256);
        }

        Biome[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;

        if (includeBiome || includeBiomeTempRain) {
            BiomeSource wcm = chunk.world.getDimensionBiomeSource();

            if (includeBiome) {
                biome = new Biome[256];
                for (int i = 0; i < 256; i++) {
                    biome[i] = chunk.method_1354(new BlockPos(i & 0xF, 0, i >> 4), wcm);
                }
            }

            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] dat = getTemperatures(wcm, getX() << 4, getZ() << 4);

                for (int i = 0; i < 256; i++) {
                    biomeTemp[i] = dat[i];
                }

                dat = wcm.method_572(null, getX() << 4, getZ() << 4, 16, 16);

                for (int i = 0; i < 256; i++) {
                    biomeRain[i] = dat[i];
                }
            }
        }

        World world = getWorld();
        return new CraftChunkSnapshot(getX(), getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome, biomeTemp, biomeRain);
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        Biome[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;

        if (includeBiome || includeBiomeTempRain) {
            BiomeSource wcm = world.getHandle().getDimensionBiomeSource();

            if (includeBiome) {
                biome = new Biome[256];
                for (int i = 0; i < 256; i++) {
                    biome[i] = world.getHandle().getBiome(new BlockPos((x << 4) + (i & 0xF), 0, (z << 4) + (i >> 4)));
                }
            }

            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] dat = getTemperatures(wcm, x << 4, z << 4);

                for (int i = 0; i < 256; i++) {
                    biomeTemp[i] = dat[i];
                }

                dat = wcm.method_572(null, x << 4, z << 4, 16, 16);

                for (int i = 0; i < 256; i++) {
                    biomeRain[i] = dat[i];
                }
            }
        }

        /* Fill with empty data */
        int hSection = world.getMaxHeight() >> 4;
        short[][] blockIDs = new short[hSection][];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        byte[][] blockData = new byte[hSection][];
        boolean[] empty = new boolean[hSection];

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIDs;
            skyLight[i] = emptySkyLight;
            emitLight[i] = emptyData;
            blockData[i] = emptyData;
            empty[i] = true;
        }

        return new CraftChunkSnapshot(x, z, world.getName(), world.getFullTime(), blockIDs, blockData, skyLight, emitLight, empty, new int[256], biome, biomeTemp, biomeRain);
    }

    private static float[] getTemperatures(BiomeSource chunkmanager, int chunkX, int chunkZ) {
        Biome[] biomes = chunkmanager.method_573(null, chunkX, chunkZ, 16, 16);
        float[] temps = new float[biomes.length];

        for (int i = 0; i < biomes.length; i++) {
            float temp = biomes[i].temperature; // Vanilla of olde: ((int) biomes[i].temperature * 65536.0F) / 65536.0F

            if (temp > 1F) {
                temp = 1F;
            }

            temps[i] = temp;
        }

        return temps;
    }

    static {
        Arrays.fill(emptySkyLight, (byte) 0xFF);
    }
}
