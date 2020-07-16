package org.bukkit.craftbukkit.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1371;
import net.minecraft.class_1712;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.CommandStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.server.*;
import net.minecraft.server.command.Console;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CommandBlockExecutor;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.*;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends VanillaCommand {
    protected final AbstractCommand vanillaCommand;

    public VanillaCommandWrapper(AbstractCommand vanillaCommand) {
        super(vanillaCommand.getName());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(AbstractCommand vanillaCommand, String usage) {
        super(vanillaCommand.getName());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        CommandSource icommandlistener = getListener(sender);
        dispatchVanillaCommand(sender, icommandlistener, args);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.method_5886(getListener(sender), args, new BlockPos(0, 0, 0));
    }

    public static CommandSender lastSender = null; // Nasty :(

    public final int dispatchVanillaCommand(CommandSender bSender, CommandSource icommandlistener, String[] as) {
        // Copied from net.minecraft.server.CommandHandler
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        ServerWorld[] prev = MinecraftServer.getServer().worlds;
        MinecraftServer server = MinecraftServer.getServer();
        server.worlds = new ServerWorld[server.worlds.length];
        server.worlds[0] = (ServerWorld) icommandlistener.getWorld();
        int bpos = 0;
        for (int pos = 1; pos < server.worlds.length; pos++) {
            ServerWorld world = server.worlds[bpos++];
            if (server.worlds[0] == world) {
                pos--;
                continue;
            }
            server.worlds[pos] = world;
        }

        try {
            if (vanillaCommand.isAccessible(icommandlistener)) {
                if (i > -1) {
                    List<Entity> list = ((List<Entity>)class_1712.method_6705(icommandlistener, as[i], Entity.class));
                    String s2 = as[i];
                    
                    icommandlistener.feedback(CommandStats.Type.AFFECTED_ENTITIES, list.size());
                    Iterator<Entity> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = iterator.next();

                        CommandSender oldSender = lastSender;
                        lastSender = bSender;
                        try {
                            as[i] = entity.getUuid().toString();
                            vanillaCommand.execute(icommandlistener, as);
                            j++;
                        } catch (class_1371 exceptionusage) {
                            TranslatableText chatmessage = new TranslatableText("commands.generic.usage", new Object[] { new TranslatableText(exceptionusage.getMessage(), exceptionusage.method_4844())});
                            chatmessage.getStyle().setColor(Formatting.RED);
                            icommandlistener.sendMessage(chatmessage);
                        } catch (CommandException commandexception) {
                            AbstractCommand.method_5673(icommandlistener, vanillaCommand, 1, commandexception.getMessage(), commandexception.method_4844());
                        } finally {
                            lastSender = oldSender;
                        }
                    }
                    as[i] = s2;
                } else {
                    icommandlistener.feedback(CommandStats.Type.AFFECTED_ENTITIES, 1);
                    vanillaCommand.execute(icommandlistener, as);
                    j++;
                }
            } else {
                TranslatableText chatmessage = new TranslatableText("commands.generic.permission", new Object[0]);
                chatmessage.getStyle().setColor(Formatting.RED);
                icommandlistener.sendMessage(chatmessage);
            }
        } catch (class_1371 exceptionusage) {
            TranslatableText chatmessage1 = new TranslatableText("commands.generic.usage", new Object[] { new TranslatableText(exceptionusage.getMessage(), exceptionusage.method_4844()) });
            chatmessage1.getStyle().setColor(Formatting.RED);
            icommandlistener.sendMessage(chatmessage1);
        } catch (CommandException commandexception) {
            AbstractCommand.method_5673(icommandlistener, vanillaCommand, 1, commandexception.getMessage(), commandexception.method_4844());
        } catch (Throwable throwable) {
            TranslatableText chatmessage3 = new TranslatableText("commands.generic.exception", new Object[0]);
            chatmessage3.getStyle().setColor(Formatting.RED);
            icommandlistener.sendMessage(chatmessage3);
            if (icommandlistener.getEntity() instanceof CommandBlockMinecartEntity) {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.getBlockPos().getX(), icommandlistener.getBlockPos().getY(), icommandlistener.getBlockPos().getZ()), throwable);
            } else if(icommandlistener instanceof CommandBlockExecutor) {
                CommandBlockExecutor listener = (CommandBlockExecutor) icommandlistener;
                MinecraftServer.LOGGER.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getBlockPos().getX(), listener.getBlockPos().getY(), listener.getBlockPos().getZ()), throwable);
            } else {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            MinecraftServer.getServer().worlds = prev;
        }
        icommandlistener.feedback(CommandStats.Type.SUCCESS_COUNT, j);
        return j;
    }

    private CommandSource getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((CommandBlockMinecartEntity) ((CraftMinecartCommand) sender).getHandle()).getCommandExecutor();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return Console.getInstance();
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    private int getPlayerListSize(String as[]) {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.method_5888(as, i) && class_1712.method_6690(as[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
