package io.github.fukkitmc.legacy.redirects;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class EntityHangingRedirects {

    public static Box calculateBoundingBox(BlockPos blockPosition, Direction direction, int width, int height) {
        double d0 = (double) blockPosition.getX() + 0.5D;
        double d1 = (double) blockPosition.getY() + 0.5D;
        double d2 = (double) blockPosition.getZ() + 0.5D;
        double d4 = width % 32 == 0 ? 0.5D : 0.0D;
        double d5 = height % 32 == 0 ? 0.5D : 0.0D;

        d0 -= (double) direction.getOffsetX() * 0.46875D;
        d2 -= (double) direction.getOffsetZ() * 0.46875D;
        d1 += d5;
        Direction enumdirection = direction.rotateYCounterclockwise();

        d0 += d4 * (double) enumdirection.getOffsetX();
        d2 += d4 * (double) enumdirection.getOffsetZ();
        double d6 = width;
        double d7 = height;
        double d8 = width;

        if (direction.getAxis() == Direction.Axis.Z) {
            d8 = 1.0D;
        } else {
            d6 = 1.0D;
        }

        d6 /= 32.0D;
        d7 /= 32.0D;
        d8 /= 32.0D;
        return new Box(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8);
    }

}
