package org.bukkit.craftbukkit.command;

import io.github.fukkitmc.legacy.extra.WorldExtra;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.CommandBlockExecutor;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final CommandBlockExecutor commandBlock;

    public CraftBlockCommandSender(CommandBlockExecutor commandBlockListenerAbstract) {
        super();
        this.commandBlock = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        return ((WorldExtra)commandBlock.getWorld()).getWorld().getBlockAt(commandBlock.getBlockPos().getX(), commandBlock.getBlockPos().getY(), commandBlock.getBlockPos().getZ());
    }

    public void sendMessage(String message) {
        for (Text component : CraftChatMessage.fromString(message)) {
            commandBlock.sendMessage(component);
        }
    }

    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    public String getName() {
        return commandBlock.getName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public CommandSource getTileEntity() {
        return commandBlock;
    }
}
