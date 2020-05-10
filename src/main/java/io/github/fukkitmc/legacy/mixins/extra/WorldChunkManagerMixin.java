package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.WorldChunkManagerExtra;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BiomeSource.class)
public class WorldChunkManagerMixin implements WorldChunkManagerExtra {
    @Override
    public Biome[] getBiomeBlock(Biome[] var1, int var2, int var3, int var4, int var5) {
        return new Biome[0];
    }
}
