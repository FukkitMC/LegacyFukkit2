package io.github.fukkitmc.legacy.extra;

import java.io.File;
import net.minecraft.nbt.CompoundTag;

public interface WorldNBTStorageExtra {

    CompoundTag getPlayerData(String s);

    File getPlayerDir();

}
