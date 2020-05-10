package io.github.fukkitmc.legacy.extra;

import java.util.Collection;
import net.minecraft.server.ServerConfigEntry;

public interface JsonListExtra {

    Collection<ServerConfigEntry> getValues();

}
