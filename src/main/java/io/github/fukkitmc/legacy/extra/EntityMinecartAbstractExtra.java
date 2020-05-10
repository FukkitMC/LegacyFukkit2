package io.github.fukkitmc.legacy.extra;

import org.bukkit.util.Vector;

public interface EntityMinecartAbstractExtra {

    Vector getFlyingVelocityMod();

    void setFlyingVelocityMod(Vector flying);

    Vector getDerailedVelocityMod();

    void setDerailedVelocityMod(Vector derailed);


}
