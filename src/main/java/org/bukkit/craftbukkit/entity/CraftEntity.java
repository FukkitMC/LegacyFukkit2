package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_1760;
import net.minecraft.class_1889;
import net.minecraft.entity.EndCrystalEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ExplodingTntEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.LeadKnotEntity;
import net.minecraft.entity.decoration.PaintingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombiePigmanEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.thrown.EggEntity;
import net.minecraft.entity.thrown.EnderPearlEntity;
import net.minecraft.entity.thrown.ExperienceBottleEntity;
import net.minecraft.entity.thrown.EyeOfEnderEntity;
import net.minecraft.entity.thrown.PotionEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.thrown.ThrowableEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.vehicle.SpawnerMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static final PermissibleBase perm = new PermissibleBase(new ServerOperator() {

        @Override
        public boolean isOp() {
            return false;
        }

        @Override
        public void setOp(boolean value) {

        }
    });
    
    protected final CraftServer server;
    protected Entity entity;
    private EntityDamageEvent lastDamageEvent;

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /**
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        if (entity instanceof LivingEntity) {
            // Players
            if (entity instanceof PlayerEntity) {
                if (entity instanceof ServerPlayerEntity) { return new CraftPlayer(server, (ServerPlayerEntity) entity); }
                else { return new CraftHumanEntity(server, (PlayerEntity) entity); }
            }
            // Water Animals
            else if (entity instanceof WaterCreatureEntity) {
                if (entity instanceof SquidEntity) { return new CraftSquid(server, (SquidEntity) entity); }
                else { return new CraftWaterMob(server, (WaterCreatureEntity) entity); }
            }
            else if (entity instanceof class_1760) {
                // Animals
                if (entity instanceof AnimalEntity) {
                    if (entity instanceof ChickenEntity) { return new CraftChicken(server, (ChickenEntity) entity); }
                    else if (entity instanceof CowEntity) {
                        if (entity instanceof MooshroomEntity) { return new CraftMushroomCow(server, (MooshroomEntity) entity); }
                        else { return new CraftCow(server, (CowEntity) entity); }
                    }
                    else if (entity instanceof PigEntity) { return new CraftPig(server, (PigEntity) entity); }
                    else if (entity instanceof TameableEntity) {
                        if (entity instanceof WolfEntity) { return new CraftWolf(server, (WolfEntity) entity); }
                        else if (entity instanceof OcelotEntity) { return new CraftOcelot(server, (OcelotEntity) entity); }
                    }
                    else if (entity instanceof SheepEntity) { return new CraftSheep(server, (SheepEntity) entity); }
                    else if (entity instanceof HorseBaseEntity) { return new CraftHorse(server, (HorseBaseEntity) entity); }
                    else if (entity instanceof RabbitEntity) { return new CraftRabbit(server, (RabbitEntity) entity); }
                    else  { return new CraftAnimals(server, (AnimalEntity) entity); }
                }
                // Monsters
                else if (entity instanceof HostileEntity) {
                    if (entity instanceof ZombieEntity) {
                        if (entity instanceof ZombiePigmanEntity) { return new CraftPigZombie(server, (ZombiePigmanEntity) entity); }
                        else { return new CraftZombie(server, (ZombieEntity) entity); }
                    }
                    else if (entity instanceof CreeperEntity) { return new CraftCreeper(server, (CreeperEntity) entity); }
                    else if (entity instanceof EndermanEntity) { return new CraftEnderman(server, (EndermanEntity) entity); }
                    else if (entity instanceof SilverfishEntity) { return new CraftSilverfish(server, (SilverfishEntity) entity); }
                    else if (entity instanceof GiantEntity) { return new CraftGiant(server, (GiantEntity) entity); }
                    else if (entity instanceof AbstractSkeletonEntity) { return new CraftSkeleton(server, (AbstractSkeletonEntity) entity); }
                    else if (entity instanceof BlazeEntity) { return new CraftBlaze(server, (BlazeEntity) entity); }
                    else if (entity instanceof WitchEntity) { return new CraftWitch(server, (WitchEntity) entity); }
                    else if (entity instanceof WitherEntity) { return new CraftWither(server, (WitherEntity) entity); }
                    else if (entity instanceof SpiderEntity) {
                        if (entity instanceof CaveSpiderEntity) { return new CraftCaveSpider(server, (CaveSpiderEntity) entity); }
                        else { return new CraftSpider(server, (SpiderEntity) entity); }
                    }
                    else if (entity instanceof EndermiteEntity) { return new CraftEndermite(server, (EndermiteEntity) entity); }
                    else if (entity instanceof GuardianEntity) { return new CraftGuardian(server, (GuardianEntity) entity); }

                    else  { return new CraftMonster(server, (HostileEntity) entity); }
                }
                else if (entity instanceof GolemEntity) {
                    if (entity instanceof SnowGolemEntity) { return new CraftSnowman(server, (SnowGolemEntity) entity); }
                    else if (entity instanceof IronGolemEntity) { return new CraftIronGolem(server, (IronGolemEntity) entity); }
                }
                else if (entity instanceof VillagerEntity) { return new CraftVillager(server, (VillagerEntity) entity); }
                else { return new CraftCreature(server, (class_1760) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof SlimeEntity) {
                if (entity instanceof MagmaCubeEntity) { return new CraftMagmaCube(server, (MagmaCubeEntity) entity); }
                else { return new CraftSlime(server, (SlimeEntity) entity); }
            }
            // Flying
            else if (entity instanceof FlyingEntity) {
                if (entity instanceof GhastEntity) { return new CraftGhast(server, (GhastEntity) entity); }
                else { return new CraftFlying(server, (FlyingEntity) entity); }
            }
            else if (entity instanceof EnderDragonEntity) {
                return new CraftEnderDragon(server, (EnderDragonEntity) entity);
            }
            // Ambient
            else if (entity instanceof AmbientEntity) {
                if (entity instanceof BatEntity) { return new CraftBat(server, (BatEntity) entity); }
                else { return new CraftAmbient(server, (AmbientEntity) entity); }
            }
            else if (entity instanceof ArmorStandEntity) { return new CraftArmorStand(server, (ArmorStandEntity) entity); }
            else  { return new CraftLivingEntity(server, (LivingEntity) entity); }
        }
        else if (entity instanceof EnderDragonPart) {
            EnderDragonPart part = (EnderDragonPart) entity;
            if (part.field_7994 instanceof EnderDragonEntity) { return new CraftEnderDragonPart(server, (EnderDragonPart) entity); }
            else { return new CraftComplexPart(server, (EnderDragonPart) entity); }
        }
        else if (entity instanceof ExperienceOrbEntity) { return new CraftExperienceOrb(server, (ExperienceOrbEntity) entity); }
        else if (entity instanceof ArrowEntity) { return new CraftArrow(server, (ArrowEntity) entity); }
        else if (entity instanceof BoatEntity) { return new CraftBoat(server, (BoatEntity) entity); }
        else if (entity instanceof ThrowableEntity) {
            if (entity instanceof EggEntity) { return new CraftEgg(server, (EggEntity) entity); }
            else if (entity instanceof SnowballEntity) { return new CraftSnowball(server, (SnowballEntity) entity); }
            else if (entity instanceof PotionEntity) { return new CraftThrownPotion(server, (PotionEntity) entity); }
            else if (entity instanceof EnderPearlEntity) { return new CraftEnderPearl(server, (EnderPearlEntity) entity); }
            else if (entity instanceof ExperienceBottleEntity) { return new CraftThrownExpBottle(server, (ExperienceBottleEntity) entity); }
        }
        else if (entity instanceof FallingBlockEntity) { return new CraftFallingSand(server, (FallingBlockEntity) entity); }
        else if (entity instanceof ProjectileEntity) {
            if (entity instanceof SmallFireballEntity) { return new CraftSmallFireball(server, (SmallFireballEntity) entity); }
            else if (entity instanceof FireballEntity) { return new CraftLargeFireball(server, (FireballEntity) entity); }
            else if (entity instanceof WitherSkullEntity) { return new CraftWitherSkull(server, (WitherSkullEntity) entity); }
            else { return new CraftFireball(server, (ProjectileEntity) entity); }
        }
        else if (entity instanceof EyeOfEnderEntity) { return new CraftEnderSignal(server, (EyeOfEnderEntity) entity); }
        else if (entity instanceof EndCrystalEntity) { return new CraftEnderCrystal(server, (EndCrystalEntity) entity); }
        else if (entity instanceof FishingBobberEntity) { return new CraftFish(server, (FishingBobberEntity) entity); }
        else if (entity instanceof ItemEntity) { return new CraftItem(server, (ItemEntity) entity); }
        else if (entity instanceof class_1889) {
            if (entity instanceof LightningBoltEntity) { return new CraftLightningStrike(server, (LightningBoltEntity) entity); }
            else { return new CraftWeather(server, (class_1889) entity); }
        }
        else if (entity instanceof AbstractMinecartEntity) {
            if (entity instanceof FurnaceMinecartEntity) { return new CraftMinecartFurnace(server, (FurnaceMinecartEntity) entity); }
            else if (entity instanceof ChestMinecartEntity) { return new CraftMinecartChest(server, (ChestMinecartEntity) entity); }
            else if (entity instanceof TntMinecartEntity) { return new CraftMinecartTNT(server, (TntMinecartEntity) entity); }
            else if (entity instanceof HopperMinecartEntity) { return new CraftMinecartHopper(server, (HopperMinecartEntity) entity); }
            else if (entity instanceof SpawnerMinecartEntity) { return new CraftMinecartMobSpawner(server, (SpawnerMinecartEntity) entity); }
            else if (entity instanceof MinecartEntity) { return new CraftMinecartRideable(server, (MinecartEntity) entity); }
            else if (entity instanceof CommandBlockMinecartEntity) { return new CraftMinecartCommand(server, (CommandBlockMinecartEntity) entity); }
        } else if (entity instanceof DecorationEntity) {
            if (entity instanceof PaintingEntity) { return new CraftPainting(server, (PaintingEntity) entity); }
            else if (entity instanceof ItemFrameEntity) { return new CraftItemFrame(server, (ItemFrameEntity) entity); }
            else if (entity instanceof LeadKnotEntity) { return new CraftLeash(server, (LeadKnotEntity) entity); }
            else { return new CraftHanging(server, (DecorationEntity) entity); }
        }
        else if (entity instanceof ExplodingTntEntity) { return new CraftTNTPrimed(server, (ExplodingTntEntity) entity); }
        else if (entity instanceof FireworkEntity) { return new CraftFirework(server, (FireworkEntity) entity); }

        throw new AssertionError("Unknown entity " + (entity == null ? null : entity.getClass()));
    }

    public Location getLocation() {
        return new Location(getWorld(), entity.x, entity.y, entity.z, entity.yaw, entity.pitch);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.x);
            loc.setY(entity.y);
            loc.setZ(entity.z);
            loc.setYaw(entity.yaw);
            loc.setPitch(entity.pitch);
        }

        return loc;
    }

    public Vector getVelocity() {
        return new Vector(entity.velocityZ, entity.velocityY, entity.velocityX);
    }

    public void setVelocity(Vector vel) {
        entity.velocityZ = vel.getX();
        entity.velocityY = vel.getY();
        entity.velocityX = vel.getZ();
        entity.velocityModified = true;
    }

    public boolean isOnGround() {
        if (entity instanceof ArrowEntity) {
            return ((ArrowEntity) entity).isInGround();
        }
        return entity.onGround;
    }

    public World getWorld() {
        return entity.world.getCraftWorld();
    }

    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, TeleportCause cause) {
        if (entity.field_7391 != null || entity.removed) {
            return false;
        }

        // If this entity is riding another entity, we must dismount before teleporting.
        entity.startRiding(null);

        // Spigot start
        if (!location.getWorld().equals(getWorld())) {
          entity.teleportTo(location, cause.equals(TeleportCause.NETHER_PORTAL));
          return true;
        }

        // entity.world = ((CraftWorld) location.getWorld()).getHandle();
        // Spigot end
        entity.updatePositionAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // entity.setLocation() throws no event, and so cannot be cancelled
        return true;
    }

    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        List<Entity> notchEntityList = entity.world.getEntitiesIn(entity, entity.getBoundingBox().expand(x, y, z), null);
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public int getEntityId() {
        return entity.getEntityId();
    }

    public int getFireTicks() {
        return entity.fireTicks;
    }

    public int getMaxFireTicks() {
        return entity.field_7355;
    }

    public void setFireTicks(int ticks) {
        entity.fireTicks = ticks;
    }

    public void remove() {
        entity.removed = true;
    }

    public boolean isDead() {
        return !entity.isAlive();
    }

    public boolean isValid() {
        return entity.isAlive() && entity.valid;
    }

    public Server getServer() {
        return server;
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null : getHandle().field_7391.getBukkitEntity();
    }

    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            ((CraftEntity) passenger).getHandle().startRiding(getHandle());
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return getHandle().field_7391 == null;
    }

    public boolean eject() {
        if (getHandle().field_7391 == null) {
            return false;
        }

        getHandle().field_7391.startRiding(null);
        return true;
    }

    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    public UUID getUniqueId() {
        return getHandle().getUuid();
    }

    public int getTicksLived() {
        return getHandle().ticksAlive;
    }

    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().ticksAlive = value;
    }

    public Entity getHandle() {
        return entity;
    }

    public void playEffect(EntityEffect type) {
        this.getHandle().world.sendEntityStatus(getHandle(), type.getData());
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CraftEntity{" + "id=" + getEntityId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity) obj;
        return (this.getEntityId() == other.getEntityId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public boolean isInsideVehicle() {
        return getHandle().hasVehicle != null;
    }

    public boolean leaveVehicle() {
        if (getHandle().hasVehicle == null) {
            return false;
        }

        getHandle().startRiding(null);
        return true;
    }

    public org.bukkit.entity.Entity getVehicle() {
        if (getHandle().hasVehicle == null) {
            return null;
        }

        return getHandle().hasVehicle.getBukkitEntity();
    }

    @Override
    public void setCustomName(String name) {
        if (name == null) {
            name = "";
        }

        getHandle().method_6934(name);
    }

    @Override
    public String getCustomName() {
        String name = getHandle().method_6959();

        if (name == null || name.length() == 0) {
            return null;
        }

        return name;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().isCustomNameVisible();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public String getName() {
        return getHandle().getTranslationKey();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return CraftEntity.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return perm.isOp();
    }

    @Override
    public void setOp(boolean value) {
        perm.setOp(value);
    }

    // Spigot start
    private final Spigot spigot = new Spigot()
    {
        @Override
        public boolean isInvulnerable()
        {
            return getHandle().isInvulnerableTo(net.minecraft.entity.damage.DamageSource.GENERIC);
        }
    };

    public Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
