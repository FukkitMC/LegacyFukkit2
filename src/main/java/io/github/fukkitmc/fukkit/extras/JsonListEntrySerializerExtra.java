package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.JsonList.JsonListEntrySerializer}
 */
public interface JsonListEntrySerializerExtra {

    com.google.gson.JsonElement serialize(net.minecraft.server.ServerConfigEntry var0, java.lang.reflect.Type var1, com.google.gson.JsonSerializationContext var2);

    net.minecraft.server.ServerConfigEntry deserialize(com.google.gson.JsonElement var0, java.lang.reflect.Type var1, com.google.gson.JsonDeserializationContext var2);
}
