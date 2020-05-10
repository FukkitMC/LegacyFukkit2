package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.CommandBlockBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftCommandBlock extends CraftBlockState implements CommandBlock {
    private final CommandBlockBlockEntity commandBlock;
    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        commandBlock = (CommandBlockBlockEntity) world.getTileEntityAt(getX(), getY(), getZ());
        command = commandBlock.getCommandExecutor().getCommand();
        name = commandBlock.getCommandExecutor().getName();
    }

    public CraftCommandBlock(final Material material, final CommandBlockBlockEntity te) {
        super(material);
        commandBlock = te;
        command = commandBlock.getCommandExecutor().getCommand();
        name = commandBlock.getCommandExecutor().getName();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            commandBlock.getCommandExecutor().setCommand(command);
            commandBlock.getCommandExecutor().setCustomName(name);
        }

        return result;
    }

    @Override
    public CommandBlockBlockEntity getTileEntity() {
        return commandBlock;
    }
}
