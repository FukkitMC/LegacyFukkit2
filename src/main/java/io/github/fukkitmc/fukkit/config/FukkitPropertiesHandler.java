package io.github.fukkitmc.fukkit.config;

import joptsimple.OptionSet;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import org.bukkit.craftbukkit.Main;

import java.io.File;

public class FukkitPropertiesHandler extends AbstractPropertiesHandler {

    public FukkitPropertiesHandler() {
        super(Main.propertyFile);
    }
}
