package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.DecorationEntity;
import net.minecraft.util.math.Direction;
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
                getHandle().setDirection(Direction.SOUTH);
                break;
            case WEST:
                getHandle().setDirection(Direction.WEST);
                break;
            case NORTH:
                getHandle().setDirection(Direction.NORTH);
                break;
            case EAST:
                getHandle().setDirection(Direction.EAST);
                break;
        }
        if (!force && !hanging.survives()) {
            // Revert since it doesn't fit
            hanging.setDirection(dir);
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
