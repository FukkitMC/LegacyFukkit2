package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.RedstoneOreBlock}
 */
public interface RedstoneOreBlockExtra {

    void e(net.minecraft.world.World var0, net.minecraft.util.math.BlockPos var1, net.minecraft.entity.Entity var2);

    int getExpDrop(net.minecraft.world.World var0, net.minecraft.block.BlockState var1, int var2);
}
