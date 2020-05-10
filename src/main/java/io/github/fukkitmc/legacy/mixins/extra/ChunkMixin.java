package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.ChunkExtra;
import net.minecraft.class_401;
import net.minecraft.server.EmptyChunk;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Chunk.class, remap = false)
public class ChunkMixin implements ChunkExtra {

    @Shadow
    public class_401[] sections;

    @Override
    public class_401[] getSections() {
        return this.sections;
    }

    @Override
    public void setNeighborLoaded(int i, int j) {

    }
}
