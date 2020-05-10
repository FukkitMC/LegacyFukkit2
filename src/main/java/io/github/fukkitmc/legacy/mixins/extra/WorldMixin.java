package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.WorldExtra;
import net.minecraft.entity.Entity;
import net.minecraft.server.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin implements WorldExtra {


    @Shadow public CraftWorld world;

    @Shadow @Final public boolean isClientSide;

    @Shadow public abstract void everyoneSleeping();

    @Shadow public abstract boolean addEntity(Entity entity);

    @Override
    public CraftWorld getWorld() {
        return this.world;
    }

    @Override
    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    @Override
    public void checkSleepStatus() {
        if (!this.isClientSide) {
            this.everyoneSleeping();
        }
    }

    @Override
    public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason) { // Changed signature, added SpawnReason
        return this.addEntity(entity);
    }

    @Override
    public void bukkitInit(ChunkGenerator gen, org.bukkit.World.Environment env) {
        this.world = new CraftWorld(((ServerWorld) (Object)this), gen, env);
        this.getServer().addWorld(this.world);
    }
}
