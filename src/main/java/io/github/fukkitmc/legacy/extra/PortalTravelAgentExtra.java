package io.github.fukkitmc.legacy.extra;

import net.minecraft.util.math.BlockPos;

public interface PortalTravelAgentExtra {

    default boolean createPortal(double x, double y, double z, int b0){
        throw new RuntimeException("createPortal not implemented for class " + getClass().getName());
    }

    default BlockPos findPortal(double x, double y, double z, int short1){
        throw new RuntimeException("findPortal not implemented for class " + getClass().getName());
    }

}
