package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.text.Text.Serializer;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.BookMeta;

import com.google.common.collect.ImmutableMap.Builder;
import org.bukkit.craftbukkit.util.CraftChatMessage;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaBookSigned extends CraftMetaBook implements BookMeta {

    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);
    }

    CraftMetaBookSigned(CompoundTag tag) {
        super(tag, false);

        boolean resolved = true;
        if (tag.contains(RESOLVED.NBT)) {
            resolved = tag.getBoolean(RESOLVED.NBT);
        }

        if (tag.contains(BOOK_PAGES.NBT)) {
            ListTag pages = tag.getList(BOOK_PAGES.NBT, 8);

            for (int i = 0; i < pages.size(); i++) {
                String page = pages.getString(i);
                if (resolved) {
                    try {
                        this.pages.add(Serializer.deserialize(page));
                        continue;
                    } catch (Exception e) {
                        // Ignore and treat as an old book
                    }
                }
                addPage(page);
            }
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);
    }

    @Override
    void applyToItem(CompoundTag itemData) {
        super.applyToItem(itemData, false);

        if (hasTitle()) {
            itemData.putString(BOOK_TITLE.NBT, this.title);
        } else {
            itemData.putString(BOOK_TITLE.NBT, " ");
        }

        if (hasAuthor()) {
            itemData.putString(BOOK_AUTHOR.NBT, this.author);
        } else {
            itemData.putString(BOOK_AUTHOR.NBT, " ");
        }

        if (hasPages()) {
            ListTag list = new ListTag();
            for (Text page : pages) {
                list.add(new StringTag(
                    Serializer.serialize(page)
                ));
            }
            itemData.put(BOOK_PAGES.NBT, list);
        }        
        itemData.putBoolean(RESOLVED.NBT, true);

        if (generation != null) {
            itemData.putInt(GENERATION.NBT, generation);
        } else {
            itemData.putInt(GENERATION.NBT, 0);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
        case WRITTEN_BOOK:
        case BOOK_AND_QUILL:
            return true;
        default:
            return false;
        }
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned) super.clone();
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }
}
