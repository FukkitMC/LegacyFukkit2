package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.AgeableEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ageable;

public class CraftAgeable extends CraftCreature implements Ageable {
    public CraftAgeable(CraftServer server, AgeableEntity entity) {
        super(server, entity);
    }

    public int getAge() {
        return getHandle().method_6893();
    }

    public void setAge(int age) {
        getHandle().method_6892(age);
    }

    public void setAgeLock(boolean lock) {
        getHandle().ageLocked = lock;
    }

    public boolean getAgeLock() {
        return getHandle().ageLocked;
    }

    public void setBaby() {
        if (isAdult()) {
            setAge(-24000);
        }
    }

    public void setAdult() {
        if (!isAdult()) {
            setAge(0);
        }
    }

    public boolean isAdult() {
        return getAge() >= 0;
    }


    public boolean canBreed() {
        return getAge() == 0;
    }

    public void setBreed(boolean breed) {
        if (breed) {
            setAge(0);
        } else if (isAdult()) {
            setAge(6000);
        }
    }

    @Override
    public AgeableEntity getHandle() {
        return (AgeableEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftAgeable";
    }
}
