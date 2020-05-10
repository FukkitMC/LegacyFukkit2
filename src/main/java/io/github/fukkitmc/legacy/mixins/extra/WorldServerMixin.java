package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.WorldServerExtra;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerWorld.class)
public class WorldServerMixin implements WorldServerExtra {

}
