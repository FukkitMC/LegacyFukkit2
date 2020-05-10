package io.github.fukkitmc.legacy.extra;

import net.minecraft.text.Text;
import org.bukkit.WeatherType;

public interface EntityPlayerExtra {

    WeatherType getPlayerWeather();

    void setPlayerWeather(WeatherType type, boolean plugin);

    void resetPlayerWeather();

    long getPlayerTime();

    void sendMessages(Text[] ichatbasecomponent);

    void updateWeather(float oldRain, float newRain, float oldThunder, float newThunder);

    String getSpawnWorld();

    void setSpawnWorld(String s);
}
