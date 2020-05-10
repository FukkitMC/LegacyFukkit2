package io.github.fukkitmc.legacy.extra;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public interface PlayerConnectionExtra {

    boolean isDisconnected();

    void teleport(Location dest);

    void chat(String s, boolean async);

    CraftPlayer getPlayer();

}
