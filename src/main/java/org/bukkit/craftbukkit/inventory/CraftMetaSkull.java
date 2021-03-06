package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtTag;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey SKULL_PROFILE = new ItemMetaKey("SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.profile = skullMeta.profile;
    }

    CraftMetaSkull(CompoundTag tag) {
        super(tag);

        if (tag.contains(SKULL_OWNER.NBT, 10)) {
            profile = NbtHelper.toGameProfile(tag.getCompound(SKULL_OWNER.NBT));
        } else if (tag.contains(SKULL_OWNER.NBT, 8) && !tag.getString(SKULL_OWNER.NBT).isEmpty()) {
            profile = new GameProfile(null, tag.getString(SKULL_OWNER.NBT));
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (profile == null) {
            setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag) {
        if (tag.contains(SKULL_PROFILE.NBT, 10)) {
            profile = NbtHelper.toGameProfile(tag.getCompound(SKULL_PROFILE.NBT));
        }
    }

    @Override
    void serializeInternal(final Map<String, NbtTag> internalTags) {
        if (profile != null) {
            CompoundTag nbtData = new CompoundTag();
            NbtHelper.fromGameProfile(nbtData, profile);
            internalTags.put(SKULL_PROFILE.NBT, nbtData);
        }
    }

    @Override
    void applyToItem(final CompoundTag tag) { // Spigot - make final
        super.applyToItem(tag);

        if (profile != null) {
            CompoundTag owner = new CompoundTag();
            NbtHelper.fromGameProfile(owner, profile);
            tag.put( SKULL_OWNER.NBT, owner );
            // Spigot start - do an async lookup of the profile. 
            // Unfortunately there is not way to refresh the holding
            // inventory, so that responsibility is left to the user.
            net.minecraft.block.entity.SkullBlockEntity.method_1188(profile);//, (Predicate<GameProfile>) input -> {
//                CompoundTag owner1 = new CompoundTag();
//                NbtHelper.fromGameProfile(owner1, input );
//                tag.put( SKULL_OWNER.NBT, owner1);
//                return false;
//            });
            // Spigot end
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return profile == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch(type) {
            case SKULL_ITEM:
                return true;
            default:
                return false;
        }
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    public boolean hasOwner() {
        return profile != null && profile.getName() != null;
    }

    public String getOwner() {
        return hasOwner() ? profile.getName() : null;
    }

    public boolean setOwner(String name) {
        if (name != null && name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            profile = null;
        } else {
            profile = new GameProfile(null, name);
        }

        return true;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasOwner()) {
            hash = 61 * hash + profile.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull) meta;

            return (this.hasOwner() ? that.hasOwner() && this.profile.equals(that.profile) : !that.hasOwner());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (hasOwner()) {
            return builder.put(SKULL_OWNER.BUKKIT, this.profile.getName());
        }
        return builder;
    }

}
