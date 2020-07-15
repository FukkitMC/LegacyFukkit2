package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements net.minecraft.inventory.Inventory {

    private final Inventory inventory;
    private final List<HumanEntity> viewers = new ArrayList<HumanEntity>();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getInvSize() {
        return inventory.getSize();
    }

    @Override
    public ItemStack getInvStack(int i) {
        return CraftItemStack.asNMSCopy(inventory.getItem(i));
    }

    @Override
    public ItemStack takeInvStack(int i, int j) {
        // Copied from CraftItemStack
        ItemStack stack = getInvStack(i);
        ItemStack result;
        if (stack == null) {
            return null;
        }
        if (stack.count <= j) {
            this.setInvStack(i, null);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, j);
            stack.count -= j;
        }
        this.markDirty();
        return result;
    }

    @Override
    public ItemStack removeInvStack(int i) {
        // Copied from CraftItemStack
        ItemStack stack = getInvStack(i);
        ItemStack result;
        if (stack == null) {
            return null;
        }
        if (stack.count <= 1) {
            this.setInvStack(i, null);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            stack.count -= 1;
        }
        return result;
    }

    @Override
    public void setInvStack(int i, ItemStack itemstack) {
        inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }

    @Override
    public int getInvMaxStackAmount() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity entityhuman) {
        return true;
    }

    @Override
    public void onInvOpen(PlayerEntity entityhuman) {
    }

    @Override
    public void onInvClose(PlayerEntity entityhuman) {
    }

    @Override
    public boolean isValidInvStack(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int method_6740(int i) {
        return 0;
    }

    @Override
    public void setProperty(int i, int j) {
    }

    @Override
    public int method_6746() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public ItemStack[] getContents() {
        int size = getInvSize();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            items[i] = getInvStack(i);
        }

        return items;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        viewers.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return viewers;
    }

    @Override
    public InventoryHolder getOwner() {
        return inventory.getHolder();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public String getTranslationKey() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return getTranslationKey() != null;
    }

    @Override
    public Text getName() {
        return CraftChatMessage.fromString(getTranslationKey())[0];
    }
}
