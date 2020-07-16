package io.github.fukkitmc.fukkit.extras;

import net.minecraft.server.ServerConfigEntry;

/**
 * Extra for {@link net.minecraft.server.ServerConfigList}
 */
public interface ServerConfigListExtra {

    java.util.Collection<ServerConfigEntry> getValues();
}
