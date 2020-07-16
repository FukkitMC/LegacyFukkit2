package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity;

import io.github.fukkitmc.fukkit.extras.EntityExtra;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin implements EntityExtra {

    @Shadow public CraftEntity bukkitEntity;

    @Shadow public World world;

    @Override
    public CraftEntity getBukkitEntity() {
        return internalGetBukkitEntity();
    }

    @Override
    public CraftEntity internalGetBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = CraftEntity.getEntity(world.getServer(), ((Entity)(Object)this));
        }
        return bukkitEntity;
    }

    @Override
    public void teleportTo(Location var0, boolean var1) {

    }

    @Override
    public void burn(float var0) {

    }
}
