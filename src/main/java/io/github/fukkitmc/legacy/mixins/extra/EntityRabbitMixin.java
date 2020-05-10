package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.EntityRabbitExtra;
import net.minecraft.entity.passive.RabbitEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RabbitEntity.class)
public class EntityRabbitMixin implements EntityRabbitExtra {

    @Override
    public void initializePathFinderGoals() {

    }
}
