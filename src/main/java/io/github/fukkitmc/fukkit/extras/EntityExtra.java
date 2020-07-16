package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.Entity}
 */
public interface EntityExtra {

    org.bukkit.craftbukkit.entity.CraftEntity getBukkitEntity();

    org.bukkit.craftbukkit.entity.CraftEntity internalGetBukkitEntity();

    void teleportTo(org.bukkit.Location var0, boolean var1);

    void burn(float var0);
}
