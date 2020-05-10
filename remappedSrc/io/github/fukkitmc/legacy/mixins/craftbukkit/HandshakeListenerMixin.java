package io.github.fukkitmc.legacy.mixins.craftbukkit;

import io.github.fukkitmc.legacy.craftbukkit.synthetic.SyntheticClass_1;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetAddress;
import java.util.HashMap;

@Mixin(ServerHandshakeNetworkHandler.class)
public class HandshakeListenerMixin {

    @Shadow public ClientConnection b;

    @Shadow public MinecraftServer a;

    @Shadow public HashMap<InetAddress, Long> throttleTracker;

    @Shadow public int throttleCounter;

    /**
     * @author Fukkit
     */
    @Overwrite
    public void a(HandshakeC2SPacket packethandshakinginsetprotocol) {
        switch (SyntheticClass_1.a[packethandshakinginsetprotocol.getIntendedState().ordinal()]) {
            case 1:
                this.b.setState(NetworkState.LOGIN);
                LiteralText chatcomponenttext;

                // CraftBukkit start - Connection throttle
                try {
                    long currentTime = System.currentTimeMillis();
                    long connectionThrottle = MinecraftServer.getServer().server.getConnectionThrottle();
                    InetAddress address = ((java.net.InetSocketAddress) this.b.getAddress()).getAddress();

                    synchronized (throttleTracker) {
                        if (throttleTracker.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress()) && currentTime - throttleTracker.get(address) < connectionThrottle) {
                            throttleTracker.put(address, currentTime);
                            chatcomponenttext = new LiteralText("Connection throttled! Please wait before reconnecting.");
                            this.b.handle(new LoginDisconnectS2CPacket(chatcomponenttext));
                            this.b.disconnect(chatcomponenttext);
                            return;
                        }

                        throttleTracker.put(address, currentTime);
                        throttleCounter++;
                        if (throttleCounter > 200) {
                            throttleCounter = 0;

                            // Cleanup stale entries
                            java.util.Iterator iter = throttleTracker.entrySet().iterator();
                            while (iter.hasNext()) {
                                java.util.Map.Entry<InetAddress, Long> entry = (java.util.Map.Entry) iter.next();
                                if (entry.getValue() > connectionThrottle) {
                                    iter.remove();
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    org.apache.logging.log4j.LogManager.getLogger().debug("Failed to check connection throttle", t);
                }
                // CraftBukkit end

                if (packethandshakinginsetprotocol.getProtocolVersion() > 47) {
                    chatcomponenttext = new LiteralText("Outdated server! I\'m still on 1.8.8");
                    this.b.handle(new LoginDisconnectS2CPacket(chatcomponenttext));
                    this.b.disconnect(chatcomponenttext);
                } else if (packethandshakinginsetprotocol.getProtocolVersion() < 47) {
                    chatcomponenttext = new LiteralText("Outdated client! Please use 1.8.8");
                    this.b.handle(new LoginDisconnectS2CPacket(chatcomponenttext));
                    this.b.disconnect(chatcomponenttext);
                } else {
                    this.b.setPacketListener((PacketListener) (new ServerLoginNetworkHandler(this.a, this.b)));
//                    ((LoginListener) this.b.getPacketListener()).hostname = packethandshakinginsetprotocol.hostname + ":" + packethandshakinginsetprotocol.port; // CraftBukkit - set hostname
                    //TODO: fukkit add login event
                }
                break;

            case 2:
                this.b.setState(NetworkState.STATUS);
                this.b.setPacketListener(new ServerQueryNetworkHandler(this.a, this.b));
                break;

            default:
                throw new UnsupportedOperationException("Invalid intention " + packethandshakinginsetprotocol.getIntendedState());
        }

    }



}
