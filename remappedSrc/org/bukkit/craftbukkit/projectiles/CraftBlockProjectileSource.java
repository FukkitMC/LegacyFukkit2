package org.bukkit.craftbukkit.projectiles;

import java.util.Random;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.thrown.EggEntity;
import net.minecraft.entity.thrown.EnderPearlEntity;
import net.minecraft.entity.thrown.ExperienceBottleEntity;
import net.minecraft.entity.thrown.PotionEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.WorldExtra;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final DispenserBlockEntity dispenserBlock;

    public CraftBlockProjectileSource(DispenserBlockEntity dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return ((WorldExtra)dispenserBlock.getWorld()).getWorld().getBlockAt(dispenserBlock.getPos().getX(), dispenserBlock.getPos().getY(), dispenserBlock.getPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        BlockPointerImpl isourceblock = new BlockPointerImpl(dispenserBlock.getWorld(), dispenserBlock.getPos());
        // Copied from DispenseBehaviorProjectile
        Position iposition = DispenserBlock.a(isourceblock);
        Direction enumdirection = DispenserBlock.b(isourceblock.f());
        net.minecraft.world.World world = dispenserBlock.getWorld();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new SnowballEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EggEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EnderPearlEntity(world, null);
            launch.setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new ExperienceBottleEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new PotionEntity(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new ArrowEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
            ((ArrowEntity) launch).fromPlayer = 1;
            ((ArrowEntity) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getOffsetX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getOffsetY() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getOffsetZ() * 0.3F);
            Random random = world.random;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getOffsetX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getOffsetY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getOffsetZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new SmallFireballEntity(world, d0, d1, d2, d3, d4, d5);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new WitherSkullEntity(world);
                launch.setPosition(d0, d1, d2);
                double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((ProjectileEntity) launch).dirX = d3 / d6 * 0.1D;
                ((ProjectileEntity) launch).dirY = d4 / d6 * 0.1D;
                ((ProjectileEntity) launch).dirZ = d5 / d6 * 0.1D;
            } else {
                launch = new FireballEntity(world);
                launch.setPosition(d0, d1, d2);
                double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((ProjectileEntity) launch).dirX = d3 / d6 * 0.1D;
                ((ProjectileEntity) launch).dirY = d4 / d6 * 0.1D;
                ((ProjectileEntity) launch).dirZ = d5 / d6 * 0.1D;
            }
            
            ((ProjectileEntity) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof net.minecraft.entity.projectile.Projectile) {
            if (launch instanceof ThrownEntity) {
                ((ThrownEntity) launch).projectileSource = this;
            }
            // Values from DispenseBehaviorProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof PotionEntity || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseBehavior classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseBehaviorProjectile
            ((net.minecraft.entity.projectile.Projectile) launch).setVelocity(enumdirection.getOffsetX(), (float) enumdirection.getOffsetY() + 0.1F, enumdirection.getOffsetZ(), b, a);
        }

        if (velocity != null) {
            ((T) ((EntityExtra)launch).getBukkitEntity()).setVelocity(velocity);
        }

        world.spawnEntity(launch);
        return (T) ((EntityExtra)launch).getBukkitEntity();
    }
}
