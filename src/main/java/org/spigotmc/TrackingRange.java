package org.spigotmc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.PaintingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class TrackingRange
{

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @param defaultRange Default range defined by Mojang
     * @return
     */
    public static int getEntityTrackingRange(Entity entity, int defaultRange)
    {
        SpigotWorldConfig config = entity.world.spigotConfig;
        if ( entity instanceof ServerPlayerEntity )
        {
            return config.playerTrackingRange;
        }  else if ( entity.activationType == 1 )
        {
            return config.monsterTrackingRange;
        } else if ( entity instanceof GhastEntity )
        {
            if ( config.monsterTrackingRange > config.monsterActivationRange )
            {
                return config.monsterTrackingRange;
            } else
            {
                return config.monsterActivationRange;
            }
        } else if ( entity.activationType == 2 )
        {
            return config.animalTrackingRange;
        } else if ( entity instanceof ItemFrameEntity || entity instanceof PaintingEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity )
        {
            return config.miscTrackingRange;
        } else 
        {
            return config.otherTrackingRange;
        }
    }
}
