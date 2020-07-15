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
        provider = world.dimension.method_1476();
    }

    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().dimension.method_1469(x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public boolean method_1321(int i, int i1) {
        return provider.method_1321(i, i1);
    }

    @Override
    public Chunk getChunk(int i, int i1) {
        return provider.getChunk(i, i1);
    }

    @Override
    public Chunk method_1326(BlockPos blockPosition) {
        return provider.method_1326(blockPosition);
    }

    @Override
    public void method_1323(ChunkProvider icp, int i, int i1) {
        provider.method_1323(icp, i, i1);
    }

    @Override
    public boolean method_1324(ChunkProvider iChunkProvider, Chunk chunk, int i, int i1) {
        return provider.method_1324(provider, chunk, i, i1);
    }

    @Override
    public boolean method_1328(boolean bln, ProgressListener ipu) {
        return provider.method_1328(bln, ipu);
    }

    @Override
    public boolean method_1330() {
        return provider.method_1330();
    }

    @Override
    public boolean method_1332() {
        return provider.method_1332();
    }

    @Override
    public List<Biome.SpawnEntry> method_1327(EntityCategory ect, BlockPos position) {
        return provider.method_1327(ect, position);
    }

    @Override
    public BlockPos method_1322(World world, String string, BlockPos position) {
        return provider.method_1322(world, string, position);
    }

    // n.m.s implementations always return 0. (The true implementation is in ChunkProviderServer)
    @Override
    public int method_1334() {
        return 0;
    }

    @Override
    public void method_1325(Chunk chunk, int i, int i1) {
        provider.method_1325(chunk, i, i1);
    }

    @Override
    public String getChunkProviderName() {
        return "NormalWorldGenerator";
    }

    @Override
    public void method_1329() {}
}
