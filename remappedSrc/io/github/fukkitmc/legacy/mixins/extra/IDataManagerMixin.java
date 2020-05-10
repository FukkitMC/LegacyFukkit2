package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.IDataManagerExtra;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;
import net.minecraft.class_635;

@Mixin(value = class_635.class, remap = false)
public interface IDataManagerMixin extends IDataManagerExtra {

    @Override
    default UUID getUUID() {
        return null;
    }

}
