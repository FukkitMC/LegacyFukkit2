package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.network.ClientConnection}
 */
public interface ClientConnectionExtra {

    void channelRead0(io.netty.channel.ChannelHandlerContext var0, net.minecraft.network.Packet var1);
}
