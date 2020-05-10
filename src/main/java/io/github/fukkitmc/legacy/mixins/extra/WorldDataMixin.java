package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.WorldDataExtra;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelProperties.class)
public class WorldDataMixin implements WorldDataExtra {
    @Override
    public void checkName(String name) {

    }
}
