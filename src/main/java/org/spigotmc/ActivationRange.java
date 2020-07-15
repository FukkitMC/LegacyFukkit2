package org.spigotmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1760;
import net.minecraft.class_1889;
import net.minecraft.entity.EndCrystalEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExplodingTntEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.thrown.ThrowableEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.craftbukkit.SpigotTimings;
import org.bukkit.entity.Creeper;

public class ActivationRange
{

    static Box maxBB = Box.createNewBox( 0, 0, 0, 0, 0, 0 );
    static Box miscBB = Box.createNewBox( 0, 0, 0, 0, 0, 0 );
    static Box animalBB = Box.createNewBox( 0, 0, 0, 0, 0, 0 );
    static Box monsterBB = Box.createNewBox( 0, 0, 0, 0, 0, 0 );

    /**
     * Initializes an entities type on construction to specify what group this
     * entity is in for activation ranges.
     *
     * @param entity
     * @return group id
     */
    public static byte initializeEntityActivationType(Entity entity)
    {
        if ( entity instanceof HostileEntity || entity instanceof SlimeEntity )
        {
            return 1; // Monster
        } else if ( entity instanceof class_1760 || entity instanceof AmbientEntity )
        {
            return 2; // Animal
        } else
        {
            return 3; // Misc
        }
    }

    /**
     * These entities are excluded from Activation range checks.
     *
     * @param entity
     * @param world
     * @return boolean If it should always tick.
     */
    public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config)
    {
        if ( ( entity.activationType == 3 && config.miscActivationRange == 0 )
                || ( entity.activationType == 2 && config.animalActivationRange == 0 )
                || ( entity.activationType == 1 && config.monsterActivationRange == 0 )
                || entity instanceof PlayerEntity
                || entity instanceof ThrowableEntity
                || entity instanceof EnderDragonEntity
                || entity instanceof EnderDragonPart
                || entity instanceof WitherEntity
                || entity instanceof ProjectileEntity
                || entity instanceof class_1889
                || entity instanceof ExplodingTntEntity
                || entity instanceof EndCrystalEntity
                || entity instanceof FireworkEntity )
        {
            return true;
        }

        return false;
    }

    /**
     * Find what entities are in range of the players in the world and set
     * active if in range.
     *
     * @param world
     */
    public static void activateEntities(World world)
    {
        SpigotTimings.entityActivationCheckTimer.startTiming();
        final int miscActivationRange = world.spigotConfig.miscActivationRange;
        final int animalActivationRange = world.spigotConfig.animalActivationRange;
        final int monsterActivationRange = world.spigotConfig.monsterActivationRange;

        int maxRange = Math.max( monsterActivationRange, animalActivationRange );
        maxRange = Math.max( maxRange, miscActivationRange );
        maxRange = Math.min( ( world.spigotConfig.viewDistance << 4 ) - 8, maxRange );

        for ( Entity player : (List<Entity>) (List) world.playerEntities )
        {

            player.activatedTick = MinecraftServer.currentTick;
            maxBB = player.getBoundingBox().expand( maxRange, 256, maxRange );
            miscBB = player.getBoundingBox().expand( miscActivationRange, 256, miscActivationRange );
            animalBB = player.getBoundingBox().expand( animalActivationRange, 256, animalActivationRange );
            monsterBB = player.getBoundingBox().expand( monsterActivationRange, 256, monsterActivationRange );

            int i = MathHelper.floor( maxBB.x1 / 16.0D );
            int j = MathHelper.floor( maxBB.x2 / 16.0D );
            int k = MathHelper.floor( maxBB.z1 / 16.0D );
            int l = MathHelper.floor( maxBB.z2 / 16.0D );

            for ( int i1 = i; i1 <= j; ++i1 )
            {
                for ( int j1 = k; j1 <= l; ++j1 )
                {
                    if ( world.getWorld().isChunkLoaded( i1, j1 ) )
                    {
                        activateChunkEntities( world.getChunk( i1, j1 ) );
                    }
                }
            }
        }
        SpigotTimings.entityActivationCheckTimer.stopTiming();
    }

    /**
     * Checks for the activation state of all entities in this chunk.
     *
     * @param chunk
     */
    private static void activateChunkEntities(Chunk chunk)
    {
        for ( List<Entity> slice : chunk.entitySlices )
        {
            for ( Entity entity : slice )
            {
                if ( MinecraftServer.currentTick > entity.activatedTick )
                {
                    if ( entity.defaultActivationState )
                    {
                        entity.activatedTick = MinecraftServer.currentTick;
                        continue;
                    }
                    switch ( entity.activationType )
                    {
                        case 1:
                            if ( monsterBB.intersects( entity.getBoundingBox() ) )
                            {
                                entity.activatedTick = MinecraftServer.currentTick;
                            }
                            break;
                        case 2:
                            if ( animalBB.intersects( entity.getBoundingBox() ) )
                            {
                                entity.activatedTick = MinecraftServer.currentTick;
                            }
                            break;
                        case 3:
                        default:
                            if ( miscBB.intersects( entity.getBoundingBox() ) )
                            {
                                entity.activatedTick = MinecraftServer.currentTick;
                            }
                    }
                }
            }
        }
    }

    /**
     * If an entity is not in range, do some more checks to see if we should
     * give it a shot.
     *
     * @param entity
     * @return
     */
    public static boolean checkEntityImmunities(Entity entity)
    {
        // quick checks.
        if ( entity.touchingWater || entity.fireTicks > 0 )
        {
            return true;
        }
        if ( !( entity instanceof ArrowEntity ) )
        {
            if ( !entity.onGround || entity.field_7391 != null
                    || entity.hasVehicle != null )
            {
                return true;
            }
        } else if ( !( (ArrowEntity) entity ).field_8398 )
        {
            return true;
        }
        // special cases.
        if ( entity instanceof LivingEntity )
        {
            LivingEntity living = (LivingEntity) entity;
            if ( /*TODO: Missed mapping? living.attackTicks > 0 || */ living.field_7464 > 0 || living.field_7488.size() > 0 )
            {
                return true;
            }
            if ( entity instanceof class_1760 && ( (class_1760) entity ).method_7226() != null )
            {
                return true;
            }
            if ( entity instanceof VillagerEntity && ( (VillagerEntity) entity ).method_7920() /* Getter for first boolean */ )
            {
                return true;
            }
            if ( entity instanceof AnimalEntity )
            {
                AnimalEntity animal = (AnimalEntity) entity;
                if ( animal.method_7163() || animal.isInLove() )
                {
                    return true;
                }
                if ( entity instanceof SheepEntity && ( (SheepEntity) entity ).isSheared() )
                {
                    return true;
                }
            }
            if (entity instanceof CreeperEntity && ((CreeperEntity) entity).method_7823()) { // isExplosive
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entity is active for this tick.
     *
     * @param entity
     * @return
     */
    public static boolean checkIfActive(Entity entity)
    {
        SpigotTimings.checkIfActiveTimer.startTiming();
        // Never safe to skip fireworks or entities not yet added to chunk
        if ( !entity.isAddedToChunk() || entity instanceof FireworkEntity ) {
            SpigotTimings.checkIfActiveTimer.stopTiming();
            return true;
        }

        boolean isActive = entity.activatedTick >= MinecraftServer.currentTick || entity.defaultActivationState;

        // Should this entity tick?
        if ( !isActive )
        {
            if ( ( MinecraftServer.currentTick - entity.activatedTick - 1 ) % 20 == 0 )
            {
                // Check immunities every 20 ticks.
                if ( checkEntityImmunities( entity ) )
                {
                    // Triggered some sort of immunity, give 20 full ticks before we check again.
                    entity.activatedTick = MinecraftServer.currentTick + 20;
                }
                isActive = true;
            }
            // Add a little performance juice to active entities. Skip 1/4 if not immune.
        } else if ( !entity.defaultActivationState && entity.ticksAlive % 4 == 0 && !checkEntityImmunities( entity ) )
        {
            isActive = false;
        }
        int x = MathHelper.floor( entity.x );
        int z = MathHelper.floor( entity.z );
        // Make sure not on edge of unloaded chunk
        Chunk chunk = entity.world.getChunkIfLoaded( x >> 4, z >> 4 );
        if ( isActive && !( chunk != null && chunk.areNeighborsLoaded( 1 ) ) )
        {
            isActive = false;
        }
        SpigotTimings.checkIfActiveTimer.stopTiming();
        return isActive;
    }
}
