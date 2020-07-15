package org.bukkit.craftbukkit.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventoryHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;

import java.util.UUID;
import net.minecraft.entity.passive.HorseBaseEntity;

public class CraftHorse extends CraftAnimals implements Horse {

    public CraftHorse(CraftServer server, HorseBaseEntity entity) {
        super(server, entity);
    }

    @Override
    public HorseBaseEntity getHandle() {
        return (HorseBaseEntity) entity;
    }

    public Variant getVariant() {
        return Variant.values()[getHandle().method_7549()];
    }

    public void setVariant(Variant variant) {
        Validate.notNull(variant, "Variant cannot be null");
        getHandle().method_7586(variant.ordinal());
    }

    public Color getColor() {
        return Color.values()[getHandle().method_7550() & 0xFF];
    }

    public void setColor(Color color) {
        Validate.notNull(color, "Color cannot be null");
        getHandle().method_7588(color.ordinal() & 0xFF | getStyle().ordinal() << 8);
    }

    public Style getStyle() {
        return Style.values()[getHandle().method_7550() >>> 8];
    }

    public void setStyle(Style style) {
        Validate.notNull(style, "Style cannot be null");
        getHandle().method_7588(getColor().ordinal() & 0xFF | style.ordinal() << 8);
    }

    public boolean isCarryingChest() {
        return getHandle().isBred();
    }

    public void setCarryingChest(boolean chest) {
        if (chest == isCarryingChest()) return;
        getHandle().method_7580(chest);
        getHandle().method_7561();
    }

    public int getDomestication() {
        return getHandle().getTemper();
    }

    public void setDomestication(int value) {
        Validate.isTrue(value >= 0, "Domestication cannot be less than zero");
        Validate.isTrue(value <= getMaxDomestication(), "Domestication cannot be greater than the max domestication");
        getHandle().setTemper(value);
    }

    public int getMaxDomestication() {
        return getHandle().getMaxTemper();
    }

    public void setMaxDomestication(int value) {
        Validate.isTrue(value > 0, "Max domestication cannot be zero or less");
        getHandle().maxDomestication = value;
    }

    public double getJumpStrength() {
        return getHandle().getJumpStrength();
    }

    public void setJumpStrength(double strength) {
        Validate.isTrue(strength >= 0, "Jump strength cannot be less than zero");
        getHandle().method_7095(HorseBaseEntity.field_7916).setBaseValue(strength);
    }

    @Override
    public boolean isTamed() {
        return getHandle().isTame();
    }

    @Override
    public void setTamed(boolean tamed) {
        getHandle().setTame(tamed);
    }

    @Override
    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) return null;
        return getServer().getOfflinePlayer(getOwnerUUID());
    }

    @Override
    public void setOwner(AnimalTamer owner) {
        if (owner != null) {
            setTamed(true);
            getHandle().setGoalTarget(null, null, false);
            setOwnerUUID(owner.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    public UUID getOwnerUUID() {
        try {
            return UUID.fromString(getHandle().method_7554());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        if (uuid == null) {
            getHandle().method_7526("");
        } else {
            getHandle().method_7526(uuid.toString());
        }
    }

    public HorseInventory getInventory() {
        return new CraftInventoryHorse(getHandle().field_7897);
    }

    @Override
    public String toString() {
        return "CraftHorse{variant=" + getVariant() + ", owner=" + getOwner() + '}';
    }

    public EntityType getType() {
        return EntityType.HORSE;
    }
}
