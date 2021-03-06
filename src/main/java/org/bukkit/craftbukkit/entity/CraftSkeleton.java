package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class CraftSkeleton extends CraftMonster implements Skeleton {

    public CraftSkeleton(CraftServer server, AbstractSkeletonEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractSkeletonEntity getHandle() {
        return (AbstractSkeletonEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    public EntityType getType() {
        return EntityType.SKELETON;
    }

    public SkeletonType getSkeletonType() {
        return SkeletonType.getType(getHandle().method_7883());
    }

    public void setSkeletonType(SkeletonType type) {
        Validate.notNull(type);
        getHandle().method_7882(type.getId());
    }
}
