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
        return ((GuardianEntity)entity).method_7860();
    }

    @Override
    public void setElder( boolean shouldBeElder ) {
        GuardianEntity entityGuardian = (GuardianEntity) entity;

        if (!isElder() && shouldBeElder) {
            entityGuardian.method_7857(true);
        } else if (isElder() && !shouldBeElder) {
            entityGuardian.method_7857(false);

            // Since minecraft does not reset the elder Guardian to a guardian we have to do that
            entity.method_6923(0.85F, 0.85F);

            // Since aW() calls its supers it will try to re register attributes which is invalid
            // check these on update
            entityGuardian.method_7095(EntityAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
            entityGuardian.method_7095(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
            entityGuardian.method_7095(EntityAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
            entityGuardian.method_7095(EntityAttributes.MAX_HEALTH).setBaseValue(30.0D);

            // Update pathfinding (random stroll back to 80)
            entityGuardian.field_8219.method_7385(80);

            // Tell minecraft that we need persistence since the guardian changed
            entityGuardian.method_7097();
        }
    }
}
