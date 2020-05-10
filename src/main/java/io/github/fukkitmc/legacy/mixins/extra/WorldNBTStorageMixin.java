package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.IDataManagerExtra;
import io.github.fukkitmc.legacy.extra.WorldNBTStorageExtra;
import net.minecraft.class_632;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.*;
import net.minecraft.world.WorldSaveHandler;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.*;
import java.util.UUID;

@Mixin(value = class_632.class)
public abstract class WorldNBTStorageMixin implements IDataManagerExtra, WorldSaveHandler, WorldNBTStorageExtra{
    @Shadow public File playerDir;

    @Shadow public static Logger a;

    @Shadow public File baseDir;

    @Shadow public UUID uuid;

    @Override
    public CompoundTag getPlayerData(String s) {
        try {
            File file1 = new File(this.playerDir, s + ".dat");

            if (file1.exists()) {
                return NbtIo.readCompressed(new FileInputStream(file1));
            }
        } catch (Exception exception) {
            a.warn("Failed to load player data for " + s);
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public File getPlayerDir() {
        return playerDir;
    }

    @Override
    public UUID getUUID() {
        if (uuid != null) return uuid;
        File file1 = new File(this.baseDir, "uid.dat");
        if (file1.exists()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(file1));
                return uuid = new UUID(dis.readLong(), dis.readLong());
            } catch (IOException ex) {
                a.warn("Failed to read " + file1 + ", generating new random UUID", ex);
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException ex) {
                        // NOOP
                    }
                }
            }
        }
        uuid = UUID.randomUUID();
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file1));
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException ex) {
            a.warn("Failed to write " + file1, ex);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                    // NOOP
                }
            }
        }
        return uuid;
    }

    /**
     * @author
     */
    @Overwrite
    public void savePlayerData(PlayerEntity entityHuman) {
        try {
            CompoundTag nBTTagCompound = new CompoundTag();
            entityHuman.writePlayerData(nBTTagCompound);
            File file = new File(this.playerDir, entityHuman.getUuid().toString() + ".dat.tmp");
            File file2 = new File(this.playerDir, entityHuman.getUuid().toString() + ".dat");
            NbtIo.writeCompressed(nBTTagCompound, new FileOutputStream(file));
            if (file2.exists()) {
                file2.delete();
            }

            file.renameTo(file2);
        } catch (Exception var5) {
            a.warn("Failed to save player data for " + entityHuman.getName());
            var5.printStackTrace();
        }

    }
}
