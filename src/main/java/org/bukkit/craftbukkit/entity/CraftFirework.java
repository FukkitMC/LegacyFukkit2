package org.bukkit.craftbukkit.entity;

import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CraftFirework extends CraftEntity implements Firework {
    private static final int FIREWORK_ITEM_INDEX = 8;

    private final Random random = new Random();
    private final CraftItemStack item;

    public CraftFirework(CraftServer server, FireworkEntity entity) {
        super(server, entity);

        ItemStack item = getHandle().getDataTracker().getStack(FIREWORK_ITEM_INDEX);

        if (item == null) {
            item = new ItemStack(Items.FIREWORKS);
            getHandle().getDataTracker().watch(FIREWORK_ITEM_INDEX, item);
        }

        this.item = CraftItemStack.asCraftMirror(item);

        // Ensure the item is a firework...
        if (this.item.getType() != Material.FIREWORK) {
            this.item.setType(Material.FIREWORK);
        }
    }

    @Override
    public FireworkEntity getHandle() {
        return (FireworkEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREWORK;
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta) item.getItemMeta();
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        item.setItemMeta(meta);

        // Copied from EntityFireworks constructor, update firework lifetime/power
        getHandle().lifeTime = 10 * (1 + meta.getPower()) + random.nextInt(6) + random.nextInt(7);

        getHandle().getDataTracker().update(FIREWORK_ITEM_INDEX);
    }

    @Override
    public void detonate() {
        getHandle().lifeTime = 0;
    }
}
