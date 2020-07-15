package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.DecorationEntity;
import net.minecraft.util.math.Direction;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, DecorationEntity entity) {
        super(server, entity);
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        DecorationEntity hanging = getHandle();
        Direction dir = hanging.direction;
        switch (face) {
            case SOUTH:
            default:
                getHandle().method_7724(Direction.SOUTH);
                break;
            case WEST:
                getHandle().method_7724(Direction.WEST);
                break;
            case NORTH:
                getHandle().method_7724(Direction.NORTH);
                break;
            case EAST:
                getHandle().method_7724(Direction.EAST);
                break;
        }
        if (!force && !hanging.method_7726()) {
            // Revert since it doesn't fit
            hanging.method_7724(dir);
            return false;
        }
        return true;
    }

    public BlockFace getFacing() {
        Direction direction = this.getHandle().direction;
        if (direction == null) return BlockFace.SELF;
        switch (direction) {
            case SOUTH:
            default:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.NORTH;
            case EAST:
                return BlockFace.EAST;
        }
    }

    @Override
    public DecorationEntity getHandle() {
        return (DecorationEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
