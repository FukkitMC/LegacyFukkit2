package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.EntityArrowExtra;
import net.minecraft.entity.projectile.ArrowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArrowEntity.class)
public class EntityArrowMixin implements EntityArrowExtra {


    @Shadow public boolean inGround;

    @Override
    public boolean isInGround() {
        return inGround;
    }
}
