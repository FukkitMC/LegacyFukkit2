package org.bukkit.craftbukkit.entity;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import net.minecraft.class_1760;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class CraftCreature extends CraftLivingEntity implements Creature {
    public CraftCreature(CraftServer server, class_1760 entity) {
        super(server, entity);
    }

    public void setTarget(LivingEntity target) {
        class_1760 entity = getHandle();
        if (target == null) {
            entity.setGoalTarget(null);
        } else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity) target).getHandle());
        }
    }

    public CraftLivingEntity getTarget() {
        if (getHandle().getGoalTarget() == null) return null;

        return (CraftLivingEntity) ((EntityExtra)getHandle().getGoalTarget()).getBukkitEntity();
    }

    @Override
    public class_1760 getHandle() {
        return (class_1760) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
