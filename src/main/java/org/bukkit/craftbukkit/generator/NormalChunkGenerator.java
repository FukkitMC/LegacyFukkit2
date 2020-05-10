package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityCategory;
import net.minecraft.server.*;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final ChunkProvider provider;

    public NormalChunkGenerator(World world, long seed) {
        provider = world.dimension.getChunkProvider();
    }

    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().dimension.canSpawn(x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public boolean isChunkLoaded(int i, int i1) {
        return provider.isChunkLoaded(i, i1);
    }

    @Override
    public Chunk getChunk(int i, int i1) {
        return provider.getChunk(i, i1);
    }

    @Override
    public Chunk getChunkAt(BlockPos blockPosition) {
        return provider.getChunkAt(blockPosition);
    }

    @Override
    public void getChunkAt(ChunkProvider icp, int i, int i1) {
        provider.getChunkAt(icp, i, i1);
    }

    @Override
    public boolean a(ChunkProvider iChunkProvider, Chunk chunk, int i, int i1) {
        return provider.a(provider, chunk, i, i1);
    }

    @Override
    public boolean saveChunks(boolean bln, ProgressListener ipu) {
        return provider.saveChunks(bln, ipu);
    }

    @Override
    public boolean unloadChunks() {
        return provider.unloadChunks();
    }

    @Override
    public boolean canSave() {
        return provider.canSave();
    }

    @Override
    public List<Biome.SpawnEntry> getMobsFor(EntityCategory ect, BlockPos position) {
        return provider.getMobsFor(ect, position);
    }

    @Override
    public BlockPos findNearestMapFeature(World world, String string, BlockPos position) {
        return provider.findNearestMapFeature(world, string, position);
    }

    // n.m.s implementations always return 0. (The true implementation is in ChunkProviderServer)
    @Override
    public int getLoadedChunks() {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int i1) {
        provider.recreateStructures(chunk, i, i1);
    }

    @Override
    public String getChunkProviderName() {
        return "NormalWorldGenerator";
    }

    @Override
    public void c() {}
}
