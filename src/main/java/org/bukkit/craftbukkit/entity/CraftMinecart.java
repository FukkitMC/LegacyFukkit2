package org.bukkit.craftbukkit.entity;

import io.github.fukkitmc.legacy.extra.EntityMinecartAbstractExtra;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {
    public CraftMinecart(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
    }

    public void setDamage(double damage) {
        getHandle().setDamageWobbleStrength((float) damage);
    }

    public double getDamage() {
        return getHandle().getDamageWobbleStrength();
    }

    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    public boolean isSlowWhenEmpty() {
        return getHandle().slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        getHandle().slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return ((EntityMinecartAbstractExtra)getHandle()).getFlyingVelocityMod();
    }

    public void setFlyingVelocityMod(Vector flying){
        ((EntityMinecartAbstractExtra)getHandle()).setFlyingVelocityMod(flying);
    }

    public Vector getDerailedVelocityMod() {
        return ((EntityMinecartAbstractExtra)getHandle()).getDerailedVelocityMod();
    }

    public void setDerailedVelocityMod(Vector derailed) {
        ((EntityMinecartAbstractExtra)getHandle()).setDerailedVelocityMod(derailed);
    }

    @Override
    public AbstractMinecartEntity getHandle() {
        return (AbstractMinecartEntity) entity;
    }

    @Deprecated
    public void _INVALID_setDamage(int damage) {
        setDamage(damage);
    }

    @Deprecated
    public int _INVALID_getDamage() {
        return NumberConversions.ceil(getDamage());
    }

    public void setDisplayBlock(MaterialData material) {
        if(material != null) {
            BlockState block = CraftMagicNumbers.getBlock(material.getItemTypeId()).stateFromData(material.getData());
            this.getHandle().setCustomBlock(block);
        } else {
            // Set block to air (default) and set the flag to not have a display block.
            this.getHandle().setCustomBlock(Blocks.AIR.getDefaultState());
            this.getHandle().a(false);
        }
    }

    public MaterialData getDisplayBlock() {
        BlockState blockData = getHandle().getDisplayBlock();
        return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte) blockData.getBlock().getData(blockData));
    }

    public void setDisplayBlockOffset(int offset) {
        getHandle().setCustomBlockOffset(offset);
    }

    public int getDisplayBlockOffset() {
        return getHandle().getDisplayBlockOffset();
    }
}
