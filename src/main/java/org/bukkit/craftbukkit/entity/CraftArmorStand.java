package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

    public CraftArmorStand(CraftServer server, ArmorStandEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public ArmorStandEntity getHandle() {
        return (ArmorStandEntity) super.getHandle();
    }

    @Override
    public ItemStack getItemInHand() {
        return getEquipment().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        getEquipment().setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment().getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        getEquipment().setHelmet(item);
    }

    @Override
    public EulerAngle getBodyPose() {
        return fromNMS(getHandle().field_8031);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        getHandle().method_7700(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return fromNMS(getHandle().field_8032);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        getHandle().method_7701(toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return fromNMS(getHandle().field_8033);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        getHandle().method_7702(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return fromNMS(getHandle().field_8034);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        getHandle().method_7703(toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return fromNMS(getHandle().field_8035);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        getHandle().method_7704(toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return fromNMS(getHandle().field_8030);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        getHandle().method_7697(toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return !getHandle().method_7714();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        getHandle().method_7709(!basePlate);
    }

    @Override
    public boolean hasGravity() {
        return !getHandle().method_7712();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().method_7707(!gravity);
    }

    @Override
    public boolean isVisible() {
        return !getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().setInvisible(!visible);
    }

    @Override
    public boolean hasArms() {
        return getHandle().method_7713();
    }

    @Override
    public void setArms(boolean arms) {
        getHandle().method_7708(arms);
    }

    @Override
    public boolean isSmall() {
        return getHandle().method_7710();
    }

    @Override
    public void setSmall(boolean small) {
        getHandle().method_7706(small);
    }

    private static EulerAngle fromNMS(net.minecraft.util.math.EulerAngle old) {
        return new EulerAngle(
            Math.toRadians(old.getPitch()),
            Math.toRadians(old.getYaw()),
            Math.toRadians(old.getRoll())
        );
    }

    private static net.minecraft.util.math.EulerAngle toNMS(EulerAngle old) {
        return new net.minecraft.util.math.EulerAngle(
            (float) Math.toDegrees(old.getX()),
            (float) Math.toDegrees(old.getY()),
            (float) Math.toDegrees(old.getZ())
        );
    }

    @Override
    public boolean isMarker() {
        // PAIL
        return getHandle().method_7715();
    }

    @Override
    public void setMarker(boolean marker) {
        // PAIL
        getHandle().method_7711(marker);
    }
}
