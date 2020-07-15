package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.PaintingEntity;
import net.minecraft.entity.decoration.PaintingEntity.Type;
import net.minecraft.server.world.ServerWorld;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, PaintingEntity entity) {
        super(server, entity);
    }

    public Art getArt() {
        Type art = getHandle().type;
        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        PaintingEntity painting = this.getHandle();
        Type oldArt = painting.type;
        painting.type = CraftArt.BukkitToNotch(art);
        painting.method_7724(painting.direction);
        if (!force && !painting.method_7726()) {
            // Revert painting since it doesn't fit
            painting.type = oldArt;
            painting.method_7724(painting.direction);
            return false;
        }
        this.update();
        return true;
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    private void update() {
        ServerWorld world = ((CraftWorld) getWorld()).getHandle();
        PaintingEntity painting = new PaintingEntity(world);
        painting.pos = getHandle().pos;
        painting.type = getHandle().type;
        painting.method_7724(getHandle().direction);
        getHandle().remove();
        getHandle().velocityModified = true; // because this occurs when the painting is broken, so it might be important
        world.spawnEntity(painting);
        this.entity = painting;
    }

    @Override
    public PaintingEntity getHandle() {
        return (PaintingEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
