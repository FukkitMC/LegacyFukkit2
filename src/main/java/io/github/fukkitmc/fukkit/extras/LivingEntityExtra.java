package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.LivingEntity}
 */
public interface LivingEntityExtra {

    void heal(float var0, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason var1);

    boolean d(net.minecraft.entity.damage.DamageSource var0, float var1);

    int getExpReward();
}
