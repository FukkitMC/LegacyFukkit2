package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.PlayerChunkMapExtra;
import net.minecraft.server.PlayerChunkMap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerChunkMap.class)
public class PlayerChunkMapMixin implements PlayerChunkMapExtra {
    @Override
    public boolean isChunkInUse(int x, int z) {
        return false;
    }
}
