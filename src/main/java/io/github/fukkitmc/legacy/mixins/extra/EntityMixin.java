package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.WorldExtra;
import net.minecraft.command.CommandStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.*;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin implements EntityExtra {

    @Shadow
    public DataTracker datawatcher;

    @Shadow
    public int fireTicks;

    @Shadow
    public float yaw;

    @Shadow
    public float pitch;
    @Shadow
    public CommandStats au;
    @Shadow
    public double R;
    @Shadow
    public World world;
    @Shadow
    public boolean invulnerable;
    @Shadow
    public int dimension;

    @Shadow
    public abstract ListTag a(double... ds);

    @Shadow
    public abstract boolean R();

    @Shadow
    public abstract UUID getUniqueID();

    @Shadow
    public abstract ListTag a(float... fs);

    @Shadow
    public abstract void b(CompoundTag nBTTagCompound);

    @Override
    public CraftEntity getBukkitEntity() {
        if (((Entity) (Object) this).bukkitEntity == null) {
            ((Entity) (Object) this).bukkitEntity = CraftEntity.getEntity(((WorldExtra)((Entity) (Object) this).world).getServer(), ((Entity) (Object) this));
        }
        return ((Entity) (Object) this).bukkitEntity;
    }
}
