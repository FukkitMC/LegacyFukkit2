package io.github.fukkitmc.fukkit.extras;

import net.minecraft.client.particle.ParticleTypes;

/**
 * Extra for {@link net.minecraft.server.world.ServerWorld}
 */
public interface ServerWorldExtra {

    void sendParticles(net.minecraft.entity.player.ServerPlayerEntity var0, ParticleTypes var1, boolean var2, double var3, double var4, double var5, int var6, double var7, double var8, double var9, double var10, int[] var11);

    net.minecraft.block.entity.BlockEntity getTileEntity(net.minecraft.util.math.BlockPos var0);

    net.minecraft.block.entity.BlockEntity fixTileEntity(net.minecraft.util.math.BlockPos var0, net.minecraft.block.Block var1, net.minecraft.block.entity.BlockEntity var2);

    boolean canSpawn(int var0, int var1);
}
