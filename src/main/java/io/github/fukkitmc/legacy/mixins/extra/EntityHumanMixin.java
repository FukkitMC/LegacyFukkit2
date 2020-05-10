package io.github.fukkitmc.legacy.mixins.extra;

import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.legacy.debug.BytecodeAnchor;
import io.github.fukkitmc.legacy.extra.EntityHumanExtra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEntity.class, remap = false)
public abstract class EntityHumanMixin extends LivingEntity implements EntityHumanExtra {

    @Shadow public String spawnWorld;

    @Shadow public PlayerInventory inventory;

    @Shadow public boolean sleeping;

    @Shadow public int sleepTicks;

    @Shadow public float exp;

    @Shadow public int expLevel;

    @Shadow public int f;

    @Shadow public int expTotal;

    @Shadow public abstract int getScore();

    @Shadow public boolean d;

    @Shadow public HungerManager foodData;

    @Shadow public PlayerAbilities abilities;

    @Shadow public EnderChestInventory enderChest;

    @Shadow public BlockPos c;

    public EntityHumanMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(World world, GameProfile gameProfile, CallbackInfo ci){
        spawnWorld = "";
    }

    @Override
    public String getSpawnWorld() {
        return this.spawnWorld;
    }

    @Override
    public void setSpawnWorld(String s){
        this.spawnWorld = s;
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void writeCustomDataToTag(CompoundTag nBTTagCompound) {
        super.writeCustomDataToTag(nBTTagCompound);
        nBTTagCompound.put("Inventory", this.inventory.serialize(new ListTag()));
        nBTTagCompound.putInt("SelectedItemSlot", this.inventory.selectedSlot);
        nBTTagCompound.putBoolean("Sleeping", this.sleeping);
        nBTTagCompound.putShort("SleepTimer", (short) this.sleepTicks);
        nBTTagCompound.putFloat("XpP", this.exp);
        nBTTagCompound.putInt("XpLevel", this.expLevel);
        nBTTagCompound.putInt("XpTotal", this.expTotal);
        nBTTagCompound.putInt("XpSeed", this.f);
        nBTTagCompound.putInt("Score", this.getScore());
        if (this.c != null) {
            nBTTagCompound.putInt("SpawnX", this.c.getX());
            nBTTagCompound.putInt("SpawnY", this.c.getY());
            nBTTagCompound.putInt("SpawnZ", this.c.getZ());
            nBTTagCompound.putBoolean("SpawnForced", this.d);
        }
        this.foodData.serialize(nBTTagCompound);
        this.abilities.serialize(nBTTagCompound);
        nBTTagCompound.put("EnderItems", this.enderChest.getTags());
        ItemStack itemstack = this.inventory.getMainHandStack();
        if (itemstack != null && itemstack.getItem() != null) {
            nBTTagCompound.put("SelectedItem", itemstack.toTag(new CompoundTag()));
        }
        nBTTagCompound.putString("SpawnWorld", getSpawnWorld()); // CraftBukkit - fixes bed spawns for multiworld worlds
    }

}
