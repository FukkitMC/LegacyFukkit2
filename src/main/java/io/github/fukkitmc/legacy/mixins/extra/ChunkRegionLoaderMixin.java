package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.ChunkRegionLoaderExtra;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.*;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ChunkRegionLoaderMixin implements ChunkRegionLoaderExtra {


    @Shadow public Map<ColumnPos, CompoundTag> b;

    @Shadow public File d;

    @Shadow public abstract Chunk a(World world, int i, int j, CompoundTag nBTTagCompound);

    public Object[] loadChunk(World world, int i, int j) throws IOException {
        ColumnPos chunkcoordintpair = new ColumnPos(i, j);
        CompoundTag nbttagcompound = this.b.get(chunkcoordintpair);

        if (nbttagcompound == null) {
            DataInputStream datainputstream = RegionFileCache.c(this.d, i, j);

            if (datainputstream == null) {
                return null;
            }

            nbttagcompound = NbtIo.read(datainputstream);
        }

        //TODO: fix
        return null;
//        return this.a(world, i, j, nbttagcompound);
    }

}
