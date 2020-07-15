package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.VillagerEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftVillager extends CraftAgeable implements Villager, InventoryHolder {
    public CraftVillager(CraftServer server, VillagerEntity entity) {
        super(server, entity);
    }

    @Override
    public VillagerEntity getHandle() {
        return (VillagerEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    public Profession getProfession() {
        return Profession.getProfession(getHandle().method_7919());
    }

    public void setProfession(Profession profession) {
        Validate.notNull(profession);
        getHandle().method_7935(profession.getId());
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().field_8281);
    }
}
