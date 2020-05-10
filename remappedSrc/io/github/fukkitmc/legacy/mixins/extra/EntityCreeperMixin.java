package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.EntityCreeperExtra;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreeperEntity.class)
public class EntityCreeperMixin implements EntityCreeperExtra{

    public void setPowered(boolean powered) {
        DataTracker dataWatcher = ((Entity) (Object) this).dataTracker;
        if (!powered) {
            dataWatcher.watch(17, (byte) 0);
        } else {
            dataWatcher.watch(17, (byte) 1);
        }
    }
}
