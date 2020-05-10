package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.EntityPlayerExtra;
import io.github.fukkitmc.legacy.extra.MinecraftServerExtra;
import io.github.fukkitmc.legacy.extra.PlayerConnectionExtra;
import net.minecraft.SharedConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Mixin(value = ServerPlayNetworkHandler.class, remap = false)
public abstract class PlayerConnectionMixin implements PlayerConnectionExtra {

    @Shadow public MinecraftServer minecraftServer;

    @Shadow public int chatThrottle;

    @Shadow public ServerPlayerEntity player;

    @Shadow @Final public ClientConnection networkManager;

    @Shadow public static Logger c;

    @Shadow public abstract void sendPacket(Packet packet);

    @Shadow public abstract void disconnect(String string);

    @Inject(method = "<init>", at=@At("TAIL"))
    public void constructor(MinecraftServer minecraftServer, ClientConnection networkManager, ServerPlayerEntity entityPlayer, CallbackInfo ci){
        ((ServerPlayNetworkHandler)(Object)this).server = minecraftServer;
        ((ServerPlayNetworkHandler)(Object)this).chatSpamField = AtomicIntegerFieldUpdater.newUpdater(ServerPlayNetworkHandler.class, "bukkitChatThrottle");
    }

    @Override
    public boolean isDisconnected() {
        return !this.player.joining && !this.networkManager.channel.config().isAutoRead();
    }

    @Override
    public void teleport(Location dest) {
        internalTeleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.emptySet());
    }

    private void internalTeleport(double d0, double d1, double d2, float f, float f1, Set set) {
//        if (Float.isNaN(f)) {
//            f = 0;
//        }
//
//        if (Float.isNaN(f1)) {
//            f1 = 0;
//        }
//        this.justTeleported = true;
//        // CraftBukkit end
//        this.checkMovement = false;
//        this.o = d0;
//        this.p = d1;
//        this.q = d2;
//        if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X)) {
//            this.o += this.player.locX;
//        }
//
//        if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y)) {
//            this.p += this.player.locY;
//        }
//
//        if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z)) {
//            this.q += this.player.locZ;
//        }
//
//        float f2 = f;
//        float f3 = f1;
//
//        if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT)) {
//            f2 = f + this.player.yaw;
//        }
//
//        if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT)) {
//            f3 = f1 + this.player.pitch;
//        }
//
//        // CraftBukkit start - update last location
//        this.lastPosX = this.o;
//        this.lastPosY = this.p;
//        this.lastPosZ = this.q;
//        this.lastYaw = f2;
//        this.lastPitch = f3;
//        // CraftBukkit end
//
//        this.player.setLocation(this.o, this.p, this.q, f2, f3);
//        this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0, d1, d2, f, f1, set));
    }

    @Override
    public void chat(String s, boolean async) {
        if (s.isEmpty() || this.player.getChatFlags() == PlayerEntity.EnumChatVisibility.HIDDEN) {
            return;
        }

        if (!async && s.startsWith("/")) {
            this.handleCommand(s);
        } else if (this.player.getChatFlags() == PlayerEntity.EnumChatVisibility.SYSTEM) {
            // Do nothing, this is coming from a plugin
        } else {
            Player player = this.getPlayer();
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
            this.minecraftServer.server.getPluginManager().callEvent(event);

            if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                queueEvent.setCancelled(event.isCancelled());
                Waitable waitable = new Waitable.Wrapper(()-> {
                        org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                        if (queueEvent.isCancelled()) {
                            return;
                        }

                        String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                        minecraftServer.console.sendMessage(message);
                        if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                            for (Object plr : ((MinecraftServerExtra)minecraftServer).getPlayerList().players) {
                                ((EntityPlayerExtra) plr).sendMessages(CraftChatMessage.fromString(message));
                            }
                        } else {
                            for (Player plr : queueEvent.getRecipients()) {
                                plr.sendMessage(message);
                            }
                        }
                        return;
                    });
                if (async) {
                    minecraftServer.processQueue.add(waitable);
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
                minecraftServer.console.sendMessage(s);
                if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                    for (Object recipient : ((MinecraftServerExtra)minecraftServer).getPlayerList().players) {
                        ((EntityPlayerExtra) recipient).sendMessages(CraftChatMessage.fromString(s));
                    }
                } else {
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(s);
                    }
                }
            }
        }
    }

    /**
     * @author fukkit
     */
    @Overwrite(remap = false)
    public void a(ChatMessageC2SPacket packetPlayInChat){
        // CraftBukkit start - async chat
        boolean isSync = packetPlayInChat.getChatMessage().startsWith("/");
        if (packetPlayInChat.getChatMessage().startsWith("/")) {
            PlayerConnectionUtils.ensureMainThread(packetPlayInChat, ((ServerPlayNetworkHandler)(Object)this), this.player.u());
        }
        // CraftBukkit end
        if (this.player.removed || this.player.getChatFlags() == PlayerEntity.EnumChatVisibility.HIDDEN) { // CraftBukkit - dead men tell no tales
            TranslatableText chatmessage = new TranslatableText("chat.cannotSend", new Object[0]);

            chatmessage.getStyle().setColor(Formatting.RED);
            this.sendPacket(new ChatMessageS2CPacket(chatmessage));
        } else {
            this.player.resetIdleTimer();
            String s = packetPlayInChat.getChatMessage();

            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i) {
                if (!SharedConstants.isValidChar(s.charAt(i))) {
                    // CraftBukkit start - threadsafety
                    if (!isSync) {
                        Waitable waitable = new Waitable.Wrapper(()-> {
                                disconnect("Illegal characters in chat");
                        });

                        this.minecraftServer.processQueue.add(waitable);

                        try {
                            waitable.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.disconnect("Illegal characters in chat");
                    }
                    // CraftBukkit end
                    return;
                }
            }

            // CraftBukkit start
            if (isSync) {
                try {
                    this.minecraftServer.server.playerCommandState = true;
                    this.handleCommand(s);
                } finally {
                    this.minecraftServer.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                c.warn(this.player.getName() + " tried to send an empty message");
            } else if (getPlayer().isConversing()) {
                getPlayer().acceptConversationInput(s);
            } else if (this.player.getChatFlags() == PlayerEntity.EnumChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
                TranslatableText chatmessage = new TranslatableText("chat.cannotSend");

                chatmessage.getStyle().setColor(Formatting.RED);
                this.sendPacket(new ChatMessageS2CPacket(chatmessage));
            } else {
                this.chat(s, true);
                // CraftBukkit end - the below is for reference. :)
            }

            // CraftBukkit start - replaced with thread safe throttle
            // this.bukkitChatThrottle += 20;
            if (((ServerPlayNetworkHandler)(Object)this).chatSpamField.addAndGet(this, 20) > 200 && !((MinecraftServerExtra)minecraftServer).getPlayerList().isOperator(this.player.getGameProfile())) {
                if (!isSync) {
                    Waitable waitable = new Waitable.Wrapper(()-> {
                            disconnect("disconnect.spam");
                            return;
                    });

                    this.minecraftServer.processQueue.add(waitable);

                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.disconnect("disconnect.spam");
                }
                // CraftBukkit end
            }

        }
    }

    /**
     * @author Legacy Fukkit
     */
    @Overwrite
    public void handleCommand(String s) {
        c.info(this.player.getName() + " issued server command: " + s);

        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
        ((ServerPlayNetworkHandler)(Object)this).server.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try {
            ((ServerPlayNetworkHandler) (Object) this).server.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1));
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(ServerPlayNetworkHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public CraftPlayer getPlayer() {
        return (this.player == null) ? null : (CraftPlayer) ((EntityExtra)this.player).getBukkitEntity();
    }

}
