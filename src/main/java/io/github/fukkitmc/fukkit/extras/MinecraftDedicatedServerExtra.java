package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.dedicated.MinecraftDedicatedServer}
 */
public interface MinecraftDedicatedServerExtra {

    net.minecraft.server.dedicated.AbstractPropertiesHandler getPropertyManager();
}
