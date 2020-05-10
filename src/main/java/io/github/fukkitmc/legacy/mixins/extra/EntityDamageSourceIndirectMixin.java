package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.EntityDamageSourceIndirectExtra;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProjectileDamageSource.class)
public class EntityDamageSourceIndirectMixin implements EntityDamageSourceIndirectExtra {
    @Override
    public Entity getProximateDamageSource() {
        return null;
    }
}
