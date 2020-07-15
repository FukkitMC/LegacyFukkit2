package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }

    static class MinecraftInventory implements Inventory {
        private final ItemStack[] items;
        private int maxStack = MAX_STACK;
        private final List<HumanEntity> viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            Validate.notNull(title, "Title cannot be null");
            this.items = new ItemStack[size];
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public int getInvSize() {
            return items.length;
        }

        public ItemStack getInvStack(int i) {
            return items[i];
        }

        public ItemStack takeInvStack(int i, int j) {
            ItemStack stack = this.getInvStack(i);
            ItemStack result;
            if (stack == null) return null;
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

        public ItemStack removeInvStack(int i) {
            ItemStack stack = this.getInvStack(i);
            ItemStack result;
            if (stack == null) return null;
            if (stack.count <= 1) {
                this.setInvStack(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.count -= 1;
            }
            return result;
        }

        public void setInvStack(int i, ItemStack itemstack) {
            items[i] = itemstack;
            if (itemstack != null && this.getInvMaxStackAmount() > 0 && itemstack.count > this.getInvMaxStackAmount()) {
                itemstack.count = this.getInvMaxStackAmount();
            }
        }

        public int getInvMaxStackAmount() {
            return maxStack;
        }

        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        public void markDirty() {}

        public boolean canPlayerUseInv(PlayerEntity entityhuman) {
            return true;
        }

        public ItemStack[] getContents() {
            return items;
        }

        public void onOpen(CraftHumanEntity who) {
            viewers.add(who);
        }

        public void onClose(CraftHumanEntity who) {
            viewers.remove(who);
        }

        public List<HumanEntity> getViewers() {
            return viewers;
        }

        public InventoryType getType() {
            return type;
        }
        
        public InventoryHolder getOwner() {
            return owner;
        }

        public boolean isValidInvStack(int i, ItemStack itemstack) {
            return true;
        }

        @Override
        public void onInvOpen(PlayerEntity entityHuman) {

        }

        @Override
        public void onInvClose(PlayerEntity entityHuman) {

        }

        @Override
        public int method_6740(int i) {
            return 0;
        }

        @Override
        public void setProperty(int i, int i1) {

        }

        @Override
        public int method_6746() {
            return 0;
        }

        @Override
        public void clear() {

        }

        @Override
        public String getTranslationKey() {
            return title;
        }

        @Override
        public boolean hasCustomName() {
            return title != null;
        }

        @Override
        public Text getName() {
            return new LiteralText(title);
        }
    }
}
