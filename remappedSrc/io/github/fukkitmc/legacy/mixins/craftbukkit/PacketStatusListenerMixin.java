package io.github.fukkitmc.legacy.mixins.craftbukkit;

import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.EntityPlayerExtra;
import io.github.fukkitmc.legacy.extra.MinecraftServerExtra;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import net.minecraft.text.LiteralText;
import org.bukkit.craftbukkit.util.CraftIconCache;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetSocketAddress;
import java.util.Iterator;

@Mixin(ServerQueryNetworkHandler.class)
public class PacketStatusListenerMixin {

    @Shadow public ClientConnection networkManager;

    @Shadow public MinecraftServer minecraftServer;

    @Shadow public boolean d;

    /**
     * @author Fukkit
     */
    @Overwrite
    public void a(QueryRequestC2SPacket packetstatusinstart) {
        if (this.d) {
            this.networkManager.disconnect(ServerQueryNetworkHandler.a);
            // CraftBukkit start - fire ping event
            return;
        }
        this.d = true;
        // this.networkManager.handle(new PacketStatusOutServerInfo(this.minecraftServer.aG()));
        final Object[] players = ((MinecraftServerExtra)minecraftServer).getPlayerList().players.toArray();
        class ServerListPingEvent extends org.bukkit.event.server.ServerListPingEvent {
            CraftIconCache icon = minecraftServer.server.getServerIcon();

            ServerListPingEvent() {
                super(((InetSocketAddress) networkManager.getAddress()).getAddress(), ((MinecraftServerExtra)minecraftServer).getMotd(), ((MinecraftServerExtra)minecraftServer).getPlayerList().getMaxPlayerCount());
            }

            @Override
            public void setServerIcon(org.bukkit.util.CachedServerIcon icon) {
                if (!(icon instanceof CraftIconCache)) {
                    throw new IllegalArgumentException(icon + " was not created by " + org.bukkit.craftbukkit.CraftServer.class);
                }
                this.icon = (CraftIconCache) icon;
            }

            @Override
            public Iterator<Player> iterator() throws UnsupportedOperationException {
                return new Iterator<Player>() {
                    int i;
                    int ret = Integer.MIN_VALUE;
                    ServerPlayerEntity player;

                    @Override
                    public boolean hasNext() {
                        if (player != null) {
                            return true;
                        }
                        for (int length = players.length, i = this.i; i < length; i++) {
                            final ServerPlayerEntity player = (ServerPlayerEntity) players[i];
                            if (player != null) {
                                this.i = i + 1;
                                this.player = player;
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public Player next() {
                        if (!hasNext()) {
                            throw new java.util.NoSuchElementException();
                        }
                        final ServerPlayerEntity player = this.player;
                        this.player = null;
                        this.ret = this.i - 1;
                        return (Player) ((EntityExtra)player).getBukkitEntity();
                    }

                    @Override
                    public void remove() {
                        final int i = this.ret;
                        if (i < 0 || players[i] == null) {
                            throw new IllegalStateException();
                        }
                        players[i] = null;
                    }
                };
            }
        }

        ServerListPingEvent event = new ServerListPingEvent();
        this.minecraftServer.server.getPluginManager().callEvent(event);

        java.util.List<GameProfile> profiles = new java.util.ArrayList<GameProfile>(players.length);
        for (Object player : players) {
            if (player != null) {
                profiles.add(((ServerPlayerEntity) player).getGameProfile());
            }
        }

        ServerMetadata.Players playerSample = new ServerMetadata.Players(event.getMaxPlayers(), profiles.size());
        playerSample.setSample(profiles.toArray(new GameProfile[0]));

        ServerMetadata ping = new ServerMetadata();
        ping.setFavicon(event.icon.value);
        ping.setDescription(new LiteralText(event.getMotd()));
        ping.setPlayers(playerSample);
        ping.setVersion(new ServerMetadata.Version(minecraftServer.getServerModName() + " " + ((MinecraftServerExtra)minecraftServer).getVersion(), 47)); // TODO: Update when protocol changes

        this.networkManager.handle(new QueryResponseS2CPacket(ping));
        // CraftBukkit end
    }

}
