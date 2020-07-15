package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.WolfEntity;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, WolfEntity wolf) {
        super(server, wolf);
    }

    public boolean isAngry() {
        return getHandle().method_7657();
    }

    public void setAngry(boolean angry) {
        getHandle().method_7661(angry);
    }

    @Override
    public WolfEntity getHandle() {
        return (WolfEntity) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().method_7658().getId());
    }

    public void setCollarColor(DyeColor color) {
        getHandle().method_7653(net.minecraft.util.DyeColor.getColorById(color.getWoolData()));
    }
}
