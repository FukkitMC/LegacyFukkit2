package io.github.fukkitmc.legacy.mixins.craftbukkit;

import io.github.fukkitmc.legacy.extra.IDataManagerExtra;
import net.minecraft.command.CommandStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.*;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin {

    @Shadow
    public DataTracker datawatcher;

    @Shadow public abstract int getAirTicks();

    @Shadow public int fireTicks;

    @Shadow public float fallDistance;

    @Shadow public float yaw;

    @Shadow public float pitch;

    @Shadow public abstract ListTag a(double... ds);

    @Shadow public abstract String getCustomName();

    @Shadow public CommandStats au;

    @Shadow public double R;

    @Shadow public abstract boolean R();

    @Shadow public abstract boolean getCustomNameVisible();

    @Shadow public World world;

    @Shadow public abstract UUID getUniqueID();

    @Shadow public int portalCooldown;

    @Shadow public boolean invulnerable;

    @Shadow public int dimension;

    @Shadow public boolean onGround;

    @Shadow public abstract ListTag a(float... fs);

    @Shadow public Entity vehicle;

    @Shadow public abstract void appendEntityCrashDetails(CrashReportSection crashReportSystemDetails);

    @Shadow public double locX;

    @Shadow public double locY;

    @Shadow public double locZ;

    @Shadow public double motZ;

    @Shadow public double motY;

    @Shadow public double motX;

    @Shadow public abstract void b(CompoundTag nBTTagCompound);

    /**
     * @author fukkit
     */
    @Overwrite(remap = false)
    public void e(CompoundTag nbttagcompound) {
        try {
            nbttagcompound.put("Pos", this.a(this.locX, this.locY, this.locZ));
            nbttagcompound.put("Motion", this.a(this.motX, this.motY, this.motZ));

            // CraftBukkit start - Checking for NaN pitch/yaw and resetting to zero
            // TODO: make sure this is the best way to address this.
            if (Float.isNaN(this.yaw)) {
                this.yaw = 0;
            }

            if (Float.isNaN(this.pitch)) {
                this.pitch = 0;
            }
            // CraftBukkit end
            nbttagcompound.put("Rotation", this.a(this.yaw, this.pitch));
            nbttagcompound.putFloat("FallDistance", this.fallDistance);
            nbttagcompound.putShort("Fire", (short) this.fireTicks);
            nbttagcompound.putShort("Air", (short) this.getAirTicks());
            nbttagcompound.putBoolean("OnGround", this.onGround);
            nbttagcompound.putInt("Dimension", this.dimension);
            nbttagcompound.putBoolean("Invulnerable", this.invulnerable);
            nbttagcompound.putInt("PortalCooldown", this.portalCooldown);
            nbttagcompound.putLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            nbttagcompound.putLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
            // CraftBukkit start
            nbttagcompound.putLong("WorldUUIDLeast", ((IDataManagerExtra)this.world.getDataManager()).getUUID().getLeastSignificantBits());
            nbttagcompound.putLong("WorldUUIDMost", ((IDataManagerExtra)this.world.getDataManager()).getUUID().getMostSignificantBits());
            nbttagcompound.putInt("Bukkit.updateLevel", 2);
            // CraftBukkit end
            if (this.getCustomName() != null && this.getCustomName().length() > 0) {
                nbttagcompound.putString("CustomName", this.getCustomName());
                nbttagcompound.putBoolean("CustomNameVisible", this.getCustomNameVisible());
            }
            this.au.b(nbttagcompound);
            if (this.R()) {
                nbttagcompound.putBoolean("Silent", this.R());
            }
            this.b(nbttagcompound);
            if (this.vehicle != null) {
                CompoundTag nbttagcompound1 = new CompoundTag();
                if (this.vehicle.saveSelfToTag(nbttagcompound1)) {
                    nbttagcompound.put("Riding", nbttagcompound1);
                }
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.create(throwable, "Saving entity NBT");
            CrashReportSection crashreportsystemdetails = crashreport.addElement("Entity being saved");

            this.appendEntityCrashDetails(crashreportsystemdetails);
            throw new RuntimeException(throwable);
        }
    }

}
