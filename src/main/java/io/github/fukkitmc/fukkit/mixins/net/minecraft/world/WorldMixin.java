package io.github.fukkitmc.fukkit.mixins.net.minecraft.world;

import io.github.fukkitmc.fukkit.extras.WorldExtra;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements WorldExtra {

    @Shadow public CraftWorld craftWorld;

    //method_247 = getSeed()

    @Shadow public Dimension dimension;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(SaveHandler handler, LevelProperties properties, Dimension dim, Profiler profiler, boolean client, CallbackInfo ci){
        this.craftWorld = new CraftWorld((ServerWorld) (Object)this, this.getServer().getGenerator("world"), org.bukkit.World.Environment.getEnvironment(this.dimension.getType()));
    }

    @Override
    public CraftWorld getCraftWorld() {
        return craftWorld;
    }

    @Override
    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    @Override
    public Chunk getChunkIfLoaded(int var0, int var1) {
        return null;
    }

    @Override
    public boolean addEntity(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return true;
    }

    @Override
    public void notifyAndUpdatePhysics(BlockPos var0, Chunk var1, Block var2, Block var3, int var4) {

    }

    @Override
    public void checkSleepStatus() {

    }
}
