package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.AnimalEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftAgeable implements Animals {

    public CraftAnimals(CraftServer server, AnimalEntity entity) {
        super(server, entity);
    }

    @Override
    public AnimalEntity getHandle() {
        return (AnimalEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftAnimals";
    }
}
