package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ServerScoreboardExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ServerScoreboardExtra}
 */
@Mixin(net.minecraft.scoreboard.ServerScoreboard.class)
public interface ServerScoreboardExtraMixin extends ServerScoreboardExtra {
}
