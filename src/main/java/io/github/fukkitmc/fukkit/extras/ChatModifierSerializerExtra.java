package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.ChatModifier.ChatModifierSerializer}
 */
public interface ChatModifierSerializerExtra {

    net.minecraft.text.Style deserialize(com.google.gson.JsonElement var0, java.lang.reflect.Type var1, com.google.gson.JsonDeserializationContext var2);

    com.google.gson.JsonElement serialize(net.minecraft.text.Style var0, java.lang.reflect.Type var1, com.google.gson.JsonSerializationContext var2);
}
