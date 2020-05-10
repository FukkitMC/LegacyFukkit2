package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GuardianEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, GuardianEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public boolean isElder() {
        return ((GuardianEntity)entity).isElder();
    }

    @Override
    public void setElder( boolean shouldBeElder ) {
        GuardianEntity entityGuardian = (GuardianEntity) entity;

        if (!isElder() && shouldBeElder) {
            entityGuardian.setElder(true);
        } else if (isElder() && !shouldBeElder) {
            entityGuardian.setElder(false);

            // Since minecraft does not reset the elder Guardian to a guardian we have to do that
            entity.setSize(0.85F, 0.85F);

            // Since aW() calls its supers it will try to re register attributes which is invalid
            // check these on update
            entityGuardian.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
            entityGuardian.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
            entityGuardian.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
            entityGuardian.getAttributeInstance(EntityAttributes.maxHealth).setBaseValue(30.0D);

            // Update pathfinding (random stroll back to 80)
            entityGuardian.goalRandomStroll.setChance(80);

        }
    }
}
