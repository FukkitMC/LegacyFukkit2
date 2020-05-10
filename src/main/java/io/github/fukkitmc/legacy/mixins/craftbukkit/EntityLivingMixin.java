package io.github.fukkitmc.legacy.mixins.craftbukkit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AbstractEntityAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = LivingEntity.class, remap = false)
public abstract class EntityLivingMixin {

    @Shadow
    public int as;
    @Shadow
    public AbstractEntityAttributeContainer c;
    @Shadow
    public Map<Integer, StatusEffectInstance> effects;
    @Shadow
    public int hurtTicks;
    @Shadow
    public int hurtTimestamp;
    @Shadow
    public int deathTicks;

    @Shadow
    public abstract AbstractEntityAttributeContainer getAttributeMap();

    @Shadow
    public abstract ItemStack[] as();

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract float getAbsorptionHearts();

    public void b(CompoundTag nBTTagCompound) {
        nBTTagCompound.putFloat("HealF", this.getHealth());
        nBTTagCompound.putShort("Health", (short) ((int) Math.ceil(this.getHealth())));
        nBTTagCompound.putShort("HurtTime", (short) this.hurtTicks);
        nBTTagCompound.putInt("HurtByTimestamp", this.hurtTimestamp);
        nBTTagCompound.putShort("DeathTime", (short) this.deathTicks);
        nBTTagCompound.putFloat("AbsorptionAmount", this.getAbsorptionHearts());
        ItemStack[] itemStacks2 = this.as();
        int k = itemStacks2.length;

        int l;
        ItemStack itemStack2;
        for (l = 0; l < k; ++l) {
            itemStack2 = itemStacks2[l];
            if (itemStack2 != null) {
                this.c.removeAll(itemStack2.getAttributes());
            }
        }

        nBTTagCompound.put("Attributes", EntityAttributes.toTag(this.getAttributeMap()));
        itemStacks2 = this.as();
        k = itemStacks2.length;

        for (l = 0; l < k; ++l) {
            itemStack2 = itemStacks2[l];
            if (itemStack2 != null) {
                this.c.replaceAll(itemStack2.getAttributes());
            }
        }

        if (!this.effects.isEmpty()) {
            ListTag nBTTagList = new ListTag();

            for (StatusEffectInstance mobEffect : this.effects.values()) {
                nBTTagList.add(mobEffect.a(new CompoundTag()));
            }

            nBTTagCompound.put("ActiveEffects", nBTTagList);
        }

    }
}
