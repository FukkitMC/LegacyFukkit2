package org.bukkit.craftbukkit.inventory;

import io.github.fukkitmc.legacy.extra.ContainerExtra;
import net.minecraft.class_1484;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer extends Container implements ContainerExtra {
    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private final int cachedSize;

    public CraftContainer(InventoryView view, int id) {
        this.view = view;
        this.syncId = id;
        // TODO: Do we need to check that it really is a CraftInventory?
        net.minecraft.inventory.Inventory top = ((CraftInventory)view.getTopInventory()).getInventory();
        net.minecraft.inventory.Inventory bottom = ((CraftInventory)view.getBottomInventory()).getInventory();
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        cachedSize = getSize();
        setupSlots(top, bottom);
    }

    public CraftContainer(final Inventory inventory, final HumanEntity player, int id) {
        this(new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return inventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return player.getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player;
            }

            @Override
            public InventoryType getType() {
                return inventory.getType();
            }
        }, id);
    }

    @Override
    public InventoryView getBukkitView() {
        return view;
    }

    @Override
    public void transferTo(Container other, CraftHumanEntity player) {

    }

    private int getSize() {
        return view.getTopInventory().getSize();
    }

    @Override
    public boolean isNotRestricted(PlayerEntity entityhuman) {
        if (cachedType == view.getType() && cachedSize == getSize() && cachedTitle.equals(view.getTitle())) {
            return true;
        }
        // If the window type has changed for some reason, update the player
        // This method will be called every tick or something, so it's
        // as good a place as any to put something like this.
        boolean typeChanged = (cachedType != view.getType());
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        if (view.getPlayer() instanceof CraftPlayer) {
            CraftPlayer player = (CraftPlayer) view.getPlayer();
            String type = getNotchInventoryType(cachedType);
            net.minecraft.inventory.Inventory top = ((CraftInventory)view.getTopInventory()).getInventory();
            net.minecraft.inventory.Inventory bottom = ((CraftInventory)view.getBottomInventory()).getInventory();
            this.b.clear();
            this.slots.clear();
            if (typeChanged) {
                setupSlots(top, bottom);
            }
            int size = getSize();
            player.getHandle().playerConnection.sendPacket(new class_1484(this.syncId, type, new LiteralText(cachedTitle), size));
            player.updateInventory();
        }
        return true;
    }

    public static String getNotchInventoryType(InventoryType type) {
        switch(type) {
        case WORKBENCH:
            return "minecraft:crafting_table";
        case FURNACE:
            return "minecraft:furnace";
        case DISPENSER:
            return "minecraft:dispenser";
        case ENCHANTING:
            return "minecraft:enchanting_table";
        case BREWING:
            return "minecraft:brewing_stand";
        case BEACON:
            return "minecraft:beacon";
        case ANVIL:
            return "minecraft:anvil";
        case HOPPER:
            return "minecraft:hopper";
        default:
            return "minecraft:chest";
        }
    }

    private void setupSlots(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        switch(cachedType) {
        case CREATIVE:
            break; // TODO: This should be an error?
        case PLAYER:
        case CHEST:
            setupChest(top, bottom);
            break;
        case DISPENSER:
            setupDispenser(top, bottom);
            break;
        case FURNACE:
            setupFurnace(top, bottom);
            break;
        case CRAFTING: // TODO: This should be an error?
        case WORKBENCH:
            setupWorkbench(top, bottom);
            break;
        case ENCHANTING:
            setupEnchanting(top, bottom);
            break;
        case BREWING:
            setupBrewing(top, bottom);
            break;
        case HOPPER:
            setupHopper(top, bottom);
            break;
        }
    }

    private void setupChest(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        int rows = top.getInvSize() / 9;
        int row;
        int col;
        // This code copied from ContainerChest
        int i = (rows - 4) * 18;
        for (row = 0; row < rows; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(top, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + i));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 161 + i));
        }
        // End copy from ContainerChest
    }

    private void setupWorkbench(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        // This code copied from ContainerWorkbench
        this.addSlot(new Slot(top, 0, 124, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlot(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from ContainerWorkbench
    }

    private void setupFurnace(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        // This code copied from ContainerFurnace
        this.addSlot(new Slot(top, 0, 56, 17));
        this.addSlot(new Slot(top, 1, 56, 53));
        this.addSlot(new Slot(top, 2, 116, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from ContainerFurnace
    }

    private void setupDispenser(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        // This code copied from ContainerDispenser
        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlot(new Slot(top, col + row * 3, 61 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from ContainerDispenser
    }

    private void setupEnchanting(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        // This code copied from ContainerEnchantTable
        this.addSlot((new Slot(top, 0, 25, 47)));

        int row;

        for (row = 0; row < 3; ++row) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(bottom, i1 + row * 9 + 9, 8 + i1 * 18, 84 + row * 18));
            }
        }

        for (row = 0; row < 9; ++row) {
            this.addSlot(new Slot(bottom, row, 8 + row * 18, 142));
        }
        // End copy from ContainerEnchantTable
    }

    private void setupBrewing(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        // This code copied from ContainerBrewingStand
        this.addSlot(new Slot(top, 0, 56, 46));
        this.addSlot(new Slot(top, 1, 79, 53));
        this.addSlot(new Slot(top, 2, 102, 46));
        this.addSlot(new Slot(top, 3, 79, 17));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(bottom, i, 8 + i * 18, 142));
        }
        // End copy from ContainerBrewingStand
    }

    private void setupHopper(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
        // This code copied from ContainerHopper
        byte b0 = 51;

        int i;

        for (i = 0; i < top.getInvSize(); ++i) {
            this.addSlot(new Slot(top, i, 44 + i * 18, 20));
        }

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(bottom, i, 8 + i * 18, 58 + b0));
        }
        // End copy from ContainerHopper
    }

    public boolean canUse(PlayerEntity entity) {
        return true;
    }
}
