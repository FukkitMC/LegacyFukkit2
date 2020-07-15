package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.OcelotEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

public class CraftOcelot extends CraftTameableAnimal implements Ocelot {
    public CraftOcelot(CraftServer server, OcelotEntity wolf) {
        super(server, wolf);
    }

    @Override
    public OcelotEntity getHandle() {
        return (OcelotEntity) entity;
    }

    public Type getCatType() {
        return Type.getType(getHandle().method_7598());
    }

    public void setCatType(Type type) {
        Validate.notNull(type, "Cat type cannot be null");
        getHandle().method_7599(type.getId());
    }

    @Override
    public EntityType getType() {
        return EntityType.OCELOT;
    }
}
