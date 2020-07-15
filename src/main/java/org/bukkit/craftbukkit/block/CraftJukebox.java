package org.bukkit.craftbukkit.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.JukeboxBlock.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.*;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockState implements Jukebox {
    private final CraftWorld world;
    private final JukeboxBlockEntity jukebox;

    public CraftJukebox(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        jukebox = (JukeboxBlockEntity) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftJukebox(final Material material, JukeboxBlockEntity te) {
        super(material);
        world = null;
        jukebox = te;
    }

    @Override
    public Material getPlaying() {
        ItemStack record = jukebox.method_887();
        if (record == null) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.getItem());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
            jukebox.method_888(null);
        } else {
            jukebox.method_888(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        }
        if (!isPlaced()) {
            return;
        }
        jukebox.markDirty();
        if (record == Material.AIR) {
            world.getHandle().setBlockState(new BlockPos(getX(), getY(), getZ()),
                Blocks.JUKEBOX.getDefaultState()
                    .with(JukeboxBlock.HAS_RECORD, false), 3);
        } else {
            world.getHandle().setBlockState(new BlockPos(getX(), getY(), getZ()),
                Blocks.JUKEBOX.getDefaultState()
                    .with(JukeboxBlock.HAS_RECORD, true), 3);
        }
        world.playEffect(getLocation(), Effect.RECORD_PLAY, record.getId());
    }

    public boolean isPlaying() {
        return getRawData() == 1;
    }

    public boolean eject() {
        requirePlaced();
        boolean result = isPlaying();
        ((JukeboxBlock) Blocks.JUKEBOX).method_886(world.getHandle(), new BlockPos(getX(), getY(), getZ()), null);
        return result;
    }

    @Override
    public JukeboxBlockEntity getTileEntity() {
        return jukebox;
    }
}
