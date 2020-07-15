package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.PortalTravelAgent}
 */
public interface PortalTravelAgentExtra {

    boolean createPortal(double var0, double var1, double var2, int var3);

    void adjustExit(net.minecraft.entity.Entity var0, org.bukkit.Location var1, org.bukkit.util.Vector var2);

    net.minecraft.util.math.BlockPos findEndPortal(net.minecraft.util.math.BlockPos var0);

    net.minecraft.util.math.BlockPos findPortal(double var0, double var1, double var2, int var3);

    net.minecraft.util.math.BlockPos createEndPortal(double var0, double var1, double var2);
}
