package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtTag;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBanner;
import org.bukkit.craftbukkit.block.CraftBeacon;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBrewingStand;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.craftbukkit.block.CraftCommandBlock;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.block.CraftDispenser;
import org.bukkit.craftbukkit.block.CraftDropper;
import org.bukkit.craftbukkit.block.CraftFurnace;
import org.bukkit.craftbukkit.block.CraftHopper;
import org.bukkit.craftbukkit.block.CraftJukebox;
import org.bukkit.craftbukkit.block.CraftNoteBlock;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.block.CraftSkull;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBlockState extends CraftMetaItem implements BlockStateMeta {
    
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    
    final Material material;
    CompoundTag blockEntityTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaBlockState)
                || ((CraftMetaBlockState) meta).material != material
                || material == Material.SIGN
                || material == Material.COMMAND) {
            blockEntityTag = null;
            return;
        }

        CraftMetaBlockState te = (CraftMetaBlockState) meta;
        this.blockEntityTag = te.blockEntityTag;
    }

    CraftMetaBlockState(CompoundTag tag, Material material) {
        super(tag);
        this.material = material;
        
        if (tag.contains(BLOCK_ENTITY_TAG.NBT, 10)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        } else {
            blockEntityTag = null;
        }
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String matName = SerializableMeta.getString(map, "blockMaterial", true);
        Material m = Material.getMaterial(matName);
        if (m != null) {
            material = m;
        } else {
            material = Material.AIR;
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);
        
        if (blockEntityTag != null) {
            tag.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag) {
        if (tag.contains(BLOCK_ENTITY_TAG.NBT, 10)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(final Map<String, NbtTag> internalTags) {
        if (blockEntityTag != null) {
            internalTags.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", material.name());
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBlockState) {
            CraftMetaBlockState that = (CraftMetaBlockState) meta;

            return Objects.equal(this.blockEntityTag, that.blockEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch(type){         
            case FURNACE:
            case CHEST:
            case TRAPPED_CHEST:
            case JUKEBOX:
            case DISPENSER:
            case DROPPER:
            case SIGN:
            case MOB_SPAWNER:
            case NOTE_BLOCK:
            case PISTON_BASE:
            case BREWING_STAND_ITEM:
            case ENCHANTMENT_TABLE:
            case COMMAND:
            case BEACON:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case HOPPER:
            case REDSTONE_COMPARATOR:
            case FLOWER_POT_ITEM:
                return true;
        }
        return false;
    }

    @Override
    public boolean hasBlockState() {
        return blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        BlockEntity te = blockEntityTag == null ? null : BlockEntity.createFromTag(blockEntityTag);

        switch (material) {
        case SIGN:
        case SIGN_POST:
        case WALL_SIGN:
            if (te == null) {
                te = new SignBlockEntity();
            }
            return new CraftSign(material, (SignBlockEntity) te);
        case CHEST:
        case TRAPPED_CHEST:
            if (te == null) {
                te = new ChestBlockEntity();
            }
            return new CraftChest(material, (ChestBlockEntity) te);
        case BURNING_FURNACE:
        case FURNACE:
            if (te == null) {
                te = new FurnaceBlockEntity();
            }
            return new CraftFurnace(material, (FurnaceBlockEntity) te);
        case DISPENSER:
            if (te == null) {
                te = new DispenserBlockEntity();
            }
            return new CraftDispenser(material, (DispenserBlockEntity) te);
        case DROPPER:
            if (te == null) {
                te = new DispenserBlockEntity();
            }
            return new CraftDropper(material, (DropperBlockEntity) te);
        case HOPPER:
            if (te == null) {
                te = new HopperBlockEntity();
            }
            return new CraftHopper(material, (HopperBlockEntity) te);
        case MOB_SPAWNER:
            if (te == null) {
                te = new MobSpawnerBlockEntity();
            }
            return new CraftCreatureSpawner(material, (MobSpawnerBlockEntity) te);
        case NOTE_BLOCK:
            if (te == null) {
                te = new NoteBlockBlockEntity();
            }
            return new CraftNoteBlock(material, (NoteBlockBlockEntity) te);
        case JUKEBOX:
            if (te == null) {
                te = new JukeboxBlock.JukeboxBlockEntity();
            }
            return new CraftJukebox(material, (JukeboxBlock.JukeboxBlockEntity) te);
        case BREWING_STAND:
            if (te == null) {
                te = new BrewingStandBlockEntity();
            }
            return new CraftBrewingStand(material, (BrewingStandBlockEntity) te);
        case SKULL:
            if (te == null) {
                te = new SkullBlockEntity();
            }
            return new CraftSkull(material, (SkullBlockEntity) te);
        case COMMAND:
            if (te == null) {
                te = new CommandBlockBlockEntity();
            }
            return new CraftCommandBlock(material, (CommandBlockBlockEntity) te);
        case BEACON:
            if (te == null) {
                te = new BeaconBlockEntity();
            }
            return new CraftBeacon(material, (BeaconBlockEntity) te);
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            if (te == null) {
                te = new BannerBlockEntity();
            }
            return new CraftBanner(material, (BannerBlockEntity) te);
        default:
            throw new IllegalStateException("Missing blockState for " + material);
        }
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Validate.notNull(blockState, "blockState must not be null");
        BlockEntity te = ((CraftBlockState) blockState).getTileEntity();
        Validate.notNull(te, "Invalid blockState");

        boolean valid;
        switch (material) {
        case SIGN:
        case SIGN_POST:
        case WALL_SIGN:
            valid = te instanceof SignBlockEntity;
            break;
        case CHEST:
        case TRAPPED_CHEST:
            valid = te instanceof ChestBlockEntity;
            break;
        case BURNING_FURNACE:
        case FURNACE:
            valid = te instanceof FurnaceBlockEntity;
            break;
        case DISPENSER:
            valid = te instanceof DispenserBlockEntity;
            break;
        case DROPPER:
            valid = te instanceof DropperBlockEntity;
            break;
        case HOPPER:
            valid = te instanceof HopperBlockEntity;
            break;
        case MOB_SPAWNER:
            valid = te instanceof MobSpawnerBlockEntity;
            break;
        case NOTE_BLOCK:
            valid = te instanceof NoteBlockBlockEntity;
            break;
        case JUKEBOX:
            valid = te instanceof JukeboxBlock.JukeboxBlockEntity;
            break;
        case BREWING_STAND:
            valid = te instanceof BrewingStandBlockEntity;
            break;
        case SKULL:
            valid = te instanceof SkullBlockEntity;
            break;
        case COMMAND:
            valid = te instanceof CommandBlockBlockEntity;
            break;
        case BEACON:
            valid = te instanceof BeaconBlockEntity;
            break;
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            valid = te instanceof BannerBlockEntity;
            break;
        default:
            valid = false;
            break;
        }

        Validate.isTrue(valid, "Invalid blockState for " + material);

        blockEntityTag = new CompoundTag();
        te.toTag(blockEntityTag);
    }
}
