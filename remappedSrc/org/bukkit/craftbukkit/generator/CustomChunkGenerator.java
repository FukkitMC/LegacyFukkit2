package org.bukkit.craftbukkit.generator;

import io.github.fukkitmc.legacy.extra.ChunkExtra;
import io.github.fukkitmc.legacy.extra.WorldChunkManagerExtra;
import io.github.fukkitmc.legacy.extra.WorldExtra;
import net.minecraft.block.Block;
import net.minecraft.class_401;
import net.minecraft.entity.EntityCategory;
import net.minecraft.server.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final ServerWorld world;
    private final Random random;
    private final WorldGenStronghold strongholdGen = new WorldGenStronghold();

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (ServerWorld) world;
        this.generator = generator;

        this.random = new Random(seed);
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public Chunk getChunk(int x, int z) {
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        Chunk chunk;

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = new net.minecraft.world.biome.Biome[256];
        ((WorldChunkManagerExtra)world.getWorldChunkManager()).getBiomeBlock(biomegrid.biome, x << 4, z << 4, 16, 16);

        // Try ChunkData method (1.8+)
        CraftChunkData data = (CraftChunkData) generator.generateChunkData(((WorldExtra)this.world).getWorld(), random, x, z, biomegrid);
        if (data != null) {
            char[][] sections = data.getRawChunkData();
            chunk = new Chunk(this.world, x, z);

            class_401[] csect = ((ChunkExtra)chunk).getSections();
            int scnt = Math.min(csect.length, sections.length);

            // Loop through returned sections
            for (int sec = 0; sec < scnt; sec++) {
                if (sections[sec] == null) {
                    continue;
                }
                char[] section = sections[sec];
                char emptyTest = 0;
                for (int i = 0; i < 4096; i++) {
                    // Filter invalid block id & data values.
                    if (Block.BLOCK_STATES.fromId(section[i]) == null) {
                        section[i] = 0;
                    }
                    emptyTest |= section[i];
                }
                // Build chunk section
                if (emptyTest != 0) {
                    csect[sec] = new class_401(sec << 4, true);
                }
            }
        } else {
            // Try extended block method (1.2+)
            short[][] xbtypes = generator.generateExtBlockSections(((WorldExtra)this.world).getWorld(), this.random, x, z, biomegrid);
            if (xbtypes != null) {
                chunk = new Chunk(this.world, x, z);

                class_401[] csect = ((ChunkExtra)chunk).getSections();
                int scnt = Math.min(csect.length, xbtypes.length);

                // Loop through returned sections
                for (int sec = 0; sec < scnt; sec++) {
                    if (xbtypes[sec] == null) {
                        continue;
                    }
                    char[] secBlkID = new char[4096]; // Allocate blk ID bytes
                    short[] bdata = xbtypes[sec];
                    for (int i = 0; i < bdata.length; i++) {
                        Block b = Block.byId(bdata[i]);
                        secBlkID[i] = (char) Block.BLOCK_STATES.getId(b.getDefaultState());
                    }
                    // Build chunk section
                    csect[sec] = new class_401(sec << 4, true);
                }
            } else { // Else check for byte-per-block section data
                byte[][] btypes = generator.generateBlockSections(((WorldExtra)this.world).getWorld(), this.random, x, z, biomegrid);

                if (btypes != null) {
                    chunk = new Chunk(this.world, x, z);

                    class_401[] csect = ((ChunkExtra)chunk).getSections();
                    int scnt = Math.min(csect.length, btypes.length);

                    for (int sec = 0; sec < scnt; sec++) {
                        if (btypes[sec] == null) {
                            continue;
                        }

                        char[] secBlkID = new char[4096]; // Allocate block ID bytes
                        for (int i = 0; i < secBlkID.length; i++) {
                            Block b = Block.byId(btypes[sec][i] & 0xFF);
                            secBlkID[i] = (char) Block.BLOCK_STATES.getId(b.getDefaultState());
                        }
                        csect[sec] = new class_401(sec << 4, true);
                    }
                } else { // Else, fall back to pre 1.2 method
                    @SuppressWarnings("deprecation")
                    byte[] types = generator.generate(((WorldExtra)this.world).getWorld(), this.random, x, z);
                    int ydim = types.length / 256;
                    int scnt = ydim / 16;

                    chunk = new Chunk(this.world, x, z); // Create empty chunk

                    class_401[] csect = ((ChunkExtra)chunk).getSections();

                    scnt = Math.min(scnt, csect.length);
                    // Loop through sections
                    for (int sec = 0; sec < scnt; sec++) {
                        class_401 cs = null; // Add sections when needed
                        char[] csbytes = null;

                        for (int cy = 0; cy < 16; cy++) {
                            int cyoff = cy | (sec << 4);

                            for (int cx = 0; cx < 16; cx++) {
                                int cxyoff = (cx * ydim * 16) + cyoff;

                                for (int cz = 0; cz < 16; cz++) {
                                    byte blk = types[cxyoff + (cz * ydim)];

                                    if (blk != 0) { // If non-empty
                                        if (cs == null) { // If no section yet, get one
                                            cs = csect[sec] = new class_401(sec << 4, true);
                                            csbytes = cs.getIdArray();
                                        }

                                        Block b = Block.byId(blk & 0xFF);
                                        csbytes[(cy << 8) | (cz << 4) | cx] = (char) Block.BLOCK_STATES.getId(b.getDefaultState());
                                    }
                                }
                            }
                        }
                        // If section built, finish prepping its state
                        if (cs != null) {
                            cs.recalcBlockCounts();
                        }
                    }
                }
            }
        }
        // Set biome grid
        byte[] biomeIndex = chunk.getBiomeIndex();
        for (int i = 0; i < biomeIndex.length; i++) {
            biomeIndex[i] = (byte) (biomegrid.biome[i].id & 0xFF);
        }
        // Initialize lighting
        chunk.initLighting();

        return chunk;
    }

    @Override
    public Chunk getChunkAt(BlockPos blockPosition) {
        return getChunkAt(blockPosition.getX() >> 4, blockPosition.getZ() >> 4);
    }

    public void getChunkAt(ChunkProvider icp, int i, int i1) {
        // Nothing!
    }

    @Override
    public boolean a(ChunkProvider iChunkProvider, Chunk chunk, int i, int i1) {
        return false;
    }

    public boolean saveChunks(boolean bln, ProgressListener ipu) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    @SuppressWarnings("deprecation")
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return generator.generate(world, random, x, z);
    }

    public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateBlockSections(world, random, x, z, biomes);
    }

    public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateExtBlockSections(world, random, x, z, biomes);
    }

    public Chunk getChunkAt(int x, int z) {
        return getChunk(x, z);
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return generator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return generator.getDefaultPopulators(world);
    }

    @Override
    public List<net.minecraft.world.biome.Biome.SpawnEntry> getMobsFor(EntityCategory type, BlockPos position) {
        net.minecraft.world.biome.Biome biomebase = world.getBiome(position);

        return biomebase == null ? null : biomebase.getMobs(type);
    }

    @Override
    public BlockPos findNearestMapFeature(World world, String type, BlockPos position) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.getNearestGeneratedFeature(world, position) : null;
    }

    public void recreateStructures(int i, int j) {
    }

    public int getLoadedChunks() {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int i1) {

    }

    public String getChunkProviderName() {
        return "CustomChunkGenerator";
    }

    public void c() {
    }

    private static class CustomBiomeGrid implements BiomeGrid {
        net.minecraft.world.biome.Biome[] biome;

        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(biome[(z << 4) | x]);
        }

        public void setBiome(int x, int z, Biome bio) {
            biome[(z << 4) | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }
}
