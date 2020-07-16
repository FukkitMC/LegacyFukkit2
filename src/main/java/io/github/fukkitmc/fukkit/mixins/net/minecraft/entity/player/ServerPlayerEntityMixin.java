package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity.player;

import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.fukkit.extras.ServerPlayerEntityExtra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerEntityExtra {


    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public void spawnIn(World var0) {

    }

    @Override
    public void tickWeather() {

    }

    @Override
    public void resetPlayerWeather() {

    }

    @Override
    public void setPlayerWeather(WeatherType var0, boolean var1) {

    }

    @Override
    public void updateWeather(float var0, float var1, float var2, float var3) {

    }

    @Override
    public int nextContainerCounter() {
        return 0;
    }

    @Override
    public void sendMessage(Text[] var0) {

    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) internalGetBukkitEntity();
    }

    @Override
    public long getPlayerTime() {
        return 0;
    }

    @Override
    public void reset() {

    }

    @Override
    public WeatherType getPlayerWeather() {
        return WeatherType.CLEAR;
    }
}
