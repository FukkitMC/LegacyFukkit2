package io.github.fukkitmc.legacy.extra;

import net.minecraft.class_401;

public interface ChunkExtra {

    class_401[] getSections();

    void setNeighborLoaded(int i, int j);

}
