package io.github.fukkitmc.legacy.mixins.craftbukkit;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.WorldExtra;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInteractManager.class)
public abstract class PlayerInteractManagerMixin {

    @Shadow public World world;

    @Shadow public ServerPlayerEntity player;

    @Shadow public LevelInfo.GameMode gamemode;

    @Shadow public abstract boolean c(BlockPos blockPosition);

    @Shadow public abstract boolean isCreative();

    /**
     * @author Fukkit
     */
    @Overwrite
    public boolean breakBlock(BlockPos blockposition) {
        // CraftBukkit start - fire BlockBreakEvent
        BlockBreakEvent event = null;

        if (this.player != null) {
            org.bukkit.block.Block block = ((WorldExtra)this.world).getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            // Sword + Creative mode pre-cancel
            boolean isSwordNoBreak = this.gamemode.isCreative() && this.player.bA() != null && this.player.bA().getItem() instanceof SwordItem;

            // Tell client the block is gone immediately then process events
            // Don't tell the client if its a creative sword break because its not broken!
            if (world.getBlockEntity(blockposition) == null && !isSwordNoBreak) {
                BlockUpdateS2CPacket packet = new BlockUpdateS2CPacket(this.world, blockposition);
                packet.block = Blocks.AIR.getDefaultState();
                ((ServerPlayerEntity) this.player).playerConnection.sendPacket(packet);
            }

            event = new BlockBreakEvent(block, (Player) ((EntityExtra)this.player).getBukkitEntity());

            // Sword + Creative mode pre-cancel
            event.setCancelled(isSwordNoBreak);

            // Calculate default block experience
            BlockState nmsData = this.world.getBlockState(blockposition);
            Block nmsBlock = nmsData.getBlock();

            if (nmsBlock != null && !event.isCancelled() && !this.isCreative() && this.player.b(nmsBlock)) {
                // Copied from block.a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity)
                if (!(nmsBlock.I() && EnchantmentHelper.hasSilkTouchEnchantment(this.player))) {
                    int data = block.getData();
                    int bonusLevel = EnchantmentHelper.getBonusBlockLootEnchantmentLevel(this.player);
                    event.setExpToDrop(0);//TODO: implement
//                    event.setExpToDrop(nmsBlock.getExpDrop(this.world, nmsData, bonusLevel));
                }
            }

            ((WorldExtra)this.world).getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (isSwordNoBreak) {
                    return false;
                }
                // Let the client know the block still exists
                ((ServerPlayerEntity) this.player).playerConnection.sendPacket(new BlockUpdateS2CPacket(this.world, blockposition));
                // Update any tile entity data for this block
                BlockEntity tileentity = this.world.getBlockEntity(blockposition);
                if (tileentity != null) {
                    this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                }
                return false;
            }
        }
        BlockState iblockdata = this.world.getBlockState(blockposition);
        if (iblockdata.getBlock() == Blocks.AIR) return false; // CraftBukkit - A plugin set block to air without cancelling
        BlockEntity tileentity = this.world.getBlockEntity(blockposition);

        // CraftBukkit start - Special case skulls, their item data comes from a tile entity
        if (iblockdata.getBlock() == Blocks.SKULL && !this.isCreative()) {
            iblockdata.getBlock().dropNaturally(world, blockposition, iblockdata, 1.0F, 0);
            return this.c(blockposition);
        }
        // CraftBukkit end

        if (this.gamemode.shouldLimitWorldModification()) {
            if (this.gamemode == LevelInfo.GameMode.SPECTATOR) {
                return false;
            }

            if (!this.player.canModifyWorld()) {
                ItemStack itemstack = this.player.bZ();

                if (itemstack == null) {
                    return false;
                }

                if (!itemstack.c(iblockdata.getBlock())) {
                    return false;
                }
            }
        }

        this.world.a(this.player, 2001, blockposition, Block.method_717(iblockdata));
        boolean flag = this.c(blockposition);

        if (this.isCreative()) {
            this.player.playerConnection.sendPacket(new BlockUpdateS2CPacket(this.world, blockposition));
        } else {
            ItemStack itemstack1 = this.player.bZ();
            boolean flag1 = this.player.b(iblockdata.getBlock());

            if (itemstack1 != null) {
                itemstack1.a(this.world, iblockdata.getBlock(), blockposition, this.player);
                if (itemstack1.count == 0) {
                    this.player.ca();
                }
            }

            if (flag && flag1) {
                iblockdata.getBlock().a(this.world, this.player, blockposition, iblockdata, tileentity);
            }
        }

        // CraftBukkit start - Drop event experience
        if (flag && event != null) {
            iblockdata.getBlock().dropExperience(this.world, blockposition, event.getExpToDrop());
        }
        // CraftBukkit end

        return flag;
    }

}
