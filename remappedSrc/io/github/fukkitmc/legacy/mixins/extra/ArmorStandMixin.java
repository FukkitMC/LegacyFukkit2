package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.EntityArmorStandExtra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandMixin extends LivingEntity implements EntityArmorStandExtra{

    public ArmorStandMixin(World world) {
        super(world);
    }

    @Override
    public void n(boolean flag) {
        byte b0 = this.dataTracker.getByte(10);

        if (flag) {
            b0 = (byte) (b0 | 16);
        } else {
            b0 &= -17;
        }

        this.dataTracker.watch(10, b0);
    }
}
