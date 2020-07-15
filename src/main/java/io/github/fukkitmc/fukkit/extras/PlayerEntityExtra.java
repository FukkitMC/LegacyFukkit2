package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.player.PlayerEntity}
 */
public interface PlayerEntityExtra {

    org.bukkit.craftbukkit.entity.CraftHumanEntity getBukkitEntity();

    boolean d(net.minecraft.entity.damage.DamageSource var0, float var1);
}
