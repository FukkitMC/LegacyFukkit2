package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.player.ServerPlayerEntity}
 */
public interface ServerPlayerEntityExtra extends LivingEntityExtra{

    void spawnIn(net.minecraft.world.World var0);

    void tickWeather();

    void resetPlayerWeather();

    void setPlayerWeather(org.bukkit.WeatherType var0, boolean var1);

    void updateWeather(float var0, float var1, float var2, float var3);

    int nextContainerCounter();

    java.lang.String toString();

    void sendMessage(net.minecraft.text.Text[] var0);

    org.bukkit.craftbukkit.entity.CraftPlayer getBukkitEntity();

    long getPlayerTime();

    void reset();

    org.bukkit.WeatherType getPlayerWeather();
}
