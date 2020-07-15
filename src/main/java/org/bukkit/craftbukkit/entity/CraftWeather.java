package org.bukkit.craftbukkit.entity;

import net.minecraft.class_1889;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Weather;

public class CraftWeather extends CraftEntity implements Weather {
    public CraftWeather(final CraftServer server, final class_1889 entity) {
        super(server, entity);
    }

    @Override
    public class_1889 getHandle() {
        return (class_1889) entity;
    }

    @Override
    public String toString() {
        return "CraftWeather";
    }

    public EntityType getType() {
        return EntityType.WEATHER;
    }
}
