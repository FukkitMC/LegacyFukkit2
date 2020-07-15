package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.bukkit.craftbukkit.CraftServer;

public class CraftItem extends CraftEntity implements Item {
    private final ItemEntity item;

    public CraftItem(CraftServer server, Entity entity, ItemEntity item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, ItemEntity entity) {
        this(server, entity, entity);
    }

    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(item.method_7765());
    }

    public void setItemStack(ItemStack stack) {
        item.method_7761(CraftItemStack.asNMSCopy(stack));
    }

    public int getPickupDelay() {
        return item.pickupDelay;
    }

    public void setPickupDelay(int delay) {
        item.pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "CraftItem";
    }

    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
