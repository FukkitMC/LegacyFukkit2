package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.EntityLivingExtra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class EntityLivingMixin implements EntityLivingExtra{
    @Override
    public int getExpReward() {
        return 0;
    }
}
