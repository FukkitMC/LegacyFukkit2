package io.github.fukkitmc.fukkit.mixins.net.minecraft.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.SharedConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public static Logger LOGGER;
    @Shadow
    public static AtomicIntegerFieldUpdater chatSpamField;
    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    public MinecraftServer server;
    @Shadow
    public CraftServer craftServer;

    @Shadow
    public abstract void disconnect(String reason);

    @Shadow
    public abstract void sendPacket(Packet packet);

    /**
     * @author Fukkit
     * @reason Spam is bad
     */
    @Overwrite
    public void onChatMessage(ChatMessageC2SPacket packet) {
        // CraftBukkit start - async chat
        boolean isSync = packet.getChatMessage().startsWith("/");
        if (packet.getChatMessage().startsWith("/")) {
            NetworkThreadUtils.forceMainThread(packet, (ServerPlayNetworkHandler) (Object) this, this.player.getServerWorld());
        }
        // CraftBukkit end
        if (this.player.removed || this.player.method_6083() == PlayerEntity.ChatVisibilityType.HIDDEN) { // CraftBukkit - dead men tell no tales
            TranslatableText chatmessage = new TranslatableText("chat.cannotSend", new Object[0]);

            chatmessage.getStyle().setColor(Formatting.RED);
            this.sendPacket(new ChatMessageS2CPacket(chatmessage));
        } else {
            this.player.updateLastActionTime();
            String s = packet.getChatMessage();

            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i) {
                if (!SharedConstants.isValidChar(s.charAt(i))) {
                    // CraftBukkit start - threadsafety
                    this.disconnect("Illegal characters in chat");
                    // CraftBukkit end
                    return;
                }
            }

            // CraftBukkit start
            if (isSync) {
                try {
                    this.server.server.playerCommandState = true;
                    this.executeCommand(s);
                } finally {
                    this.server.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                LOGGER.warn(this.player.displayName + " tried to send an empty message");
            } else if (Bukkit.getPlayer(player.getUuid()).isConversing()) {
                // Spigot start
                final String message = s;

                this.server.processQueue.add(new Waitable.Wrapper(() -> {
                    Bukkit.getPlayer(player.getUuid()).acceptConversationInput(message);
                }));
                // Spigot end
            } else if (this.player.method_6083() == PlayerEntity.ChatVisibilityType.SYSTEM) { // Re-add "Command Only" flag check
                TranslatableText chatmessage = new TranslatableText("chat.cannotSend");

                chatmessage.getStyle().setColor(Formatting.RED);
                this.sendPacket(new ChatMessageS2CPacket(chatmessage));
            } else {
                this.chat(s, true);
                // CraftBukkit end - the below is for reference. :)
            }

            // Spigot start - spam exclusions
            boolean counted = true;
            for (String exclude : org.spigotmc.SpigotConfig.spamExclusions) {
                if (exclude != null && s.startsWith(exclude)) {
                    counted = false;
                    break;
                }
            }

        }
    }

    // CraftBukkit start - add method
    public void chat(String s, boolean async) {
        if (s.isEmpty() || this.player.method_6083() == PlayerEntity.ChatVisibilityType.HIDDEN) {
            return;
        }

        if (!async && s.startsWith("/")) {
            this.executeCommand(s);
        } else if (this.player.method_6083() == PlayerEntity.ChatVisibilityType.SYSTEM) {
            // Do nothing, this is coming from a plugin
        } else {
            Player player = Bukkit.getPlayer(this.player.getUuid());
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
            this.craftServer.getPluginManager().callEvent(event);

            if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                // Evil plugins still listening to deprecated event
                final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                queueEvent.setCancelled(event.isCancelled());
                Waitable waitable = new Waitable.Wrapper(() -> {
                    org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                    if (queueEvent.isCancelled()) {
                        return;
                    }

                    String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
//                    CraftServer.console.sendMessage(message);
                    System.out.println(message); //Fukkit: yes
                    if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                        for (Object plr : ((ServerPlayNetworkHandler)(Object)this).server.getPlayerManager().players) {
                            ((ServerPlayerEntity) plr).sendMessage(CraftChatMessage.fromString(message));
                        }
                    } else {
                        for (Player plr : queueEvent.getRecipients()) {
                            plr.sendMessage(message);
                        }
                    }
                });
                if (async) {
                    server.processQueue.add(waitable);
                } else {
                    waitable.run();
                }
                try {
                    waitable.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                } catch (ExecutionException e) {
                    throw new RuntimeException("Exception processing chat event", e.getCause());
                }
            } else {
                if (event.isCancelled()) {
                    return;
                }

                s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
//                CraftServer.console.sendMessage(s);
                System.out.println(s); //Fukkit Yes
                if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                    for (Object recipient : server.getPlayerManager().players) {
                        ((ServerPlayerEntity) recipient).sendMessage(CraftChatMessage.fromString(s));
                    }
                } else {
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(s);
                    }
                }
            }
        }
    }
    // CraftBukkit end

    /**
     * @author legacy fukkit
     * @reason read line 30
     */
    @Overwrite
    public void executeCommand(String s) {
        org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.startTiming(); // Spigot
        // CraftBukkit start - whole method
        LOGGER.info(this.player.getName().getString() + " issued server command: " + s);

        CraftPlayer player = (CraftPlayer) Bukkit.getPlayer(this.player.getUuid());

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
            return;
        }

        try {
            if (Bukkit.getServer().dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(ClientConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
            return;
        }
        org.bukkit.craftbukkit.SpigotTimings.playerCommandTimer.stopTiming(); // Spigot
        // this.minecraftServer.getCommandHandler().a(this.player, s);
        // CraftBukkit end
    }

}
