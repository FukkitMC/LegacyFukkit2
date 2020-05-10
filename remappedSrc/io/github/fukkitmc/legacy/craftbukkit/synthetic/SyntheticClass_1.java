package io.github.fukkitmc.legacy.craftbukkit.synthetic;

import net.minecraft.network.NetworkState;

public class SyntheticClass_1 {

    public static final int[] a = new int[NetworkState.values().length];

    static {
        try {
            SyntheticClass_1.a[NetworkState.LOGIN.ordinal()] = 1;
        } catch (NoSuchFieldError ignored) {
            ;
        }

        try {
            SyntheticClass_1.a[NetworkState.STATUS.ordinal()] = 2;
        } catch (NoSuchFieldError ignored) {
            ;
        }

    }
}