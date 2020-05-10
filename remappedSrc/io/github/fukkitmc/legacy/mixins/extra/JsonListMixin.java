package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.JsonListExtra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Map;
import net.minecraft.server.ServerConfigEntry;
import net.minecraft.server.ServerConfigList;

@Mixin(ServerConfigList.class)
public class JsonListMixin<V> implements JsonListExtra {


    @Shadow public Map<String, V> d;

    @Override
    public Collection<ServerConfigEntry> getValues() {
        return (Collection<ServerConfigEntry>) this.d.values();
    }
}
