package org.bukkit.craftbukkit;

import net.minecraft.entity.decoration.PaintingEntity.Type;
import org.bukkit.Art;

// Safety class, will break if either side changes
public class CraftArt {

    public static Art NotchToBukkit(Type art) {
        switch (art) {
            case KEBAB: return Art.KEBAB;
            case AZTEC: return Art.AZTEC;
            case ALBAN: return Art.ALBAN;
            case AZTEC2: return Art.AZTEC2;
            case BOMB: return Art.BOMB;
            case PLANT: return Art.PLANT;
            case WASTELAND: return Art.WASTELAND;
            case POOL: return Art.POOL;
            case COURBET: return Art.COURBET;
            case SEA: return Art.SEA;
            case SUNSET: return Art.SUNSET;
            case CREEBET: return Art.CREEBET;
            case WANDERER: return Art.WANDERER;
            case GRAHAM: return Art.GRAHAM;
            case MATCH: return Art.MATCH;
            case BUST: return Art.BUST;
            case STAGE: return Art.STAGE;
            case VOID: return Art.VOID;
            case SKULL_AND_ROSES: return Art.SKULL_AND_ROSES;
            case FIGHTERS: return Art.FIGHTERS;
            case POINTER: return Art.POINTER;
            case PIGSCENE: return Art.PIGSCENE;
            case BURNING_SKULL: return Art.BURNINGSKULL;
            case SKELETON: return Art.SKELETON;
            case DONKEY_KONG: return Art.DONKEYKONG;
            case WITHER: return Art.WITHER;
            default:
                throw new AssertionError(art);
        }
    }

    public static Type BukkitToNotch(Art art) {
        switch (art) {
            case KEBAB: return Type.KEBAB;
            case AZTEC: return Type.AZTEC;
            case ALBAN: return Type.ALBAN;
            case AZTEC2: return Type.AZTEC2;
            case BOMB: return Type.BOMB;
            case PLANT: return Type.PLANT;
            case WASTELAND: return Type.WASTELAND;
            case POOL: return Type.POOL;
            case COURBET: return Type.COURBET;
            case SEA: return Type.SEA;
            case SUNSET: return Type.SUNSET;
            case CREEBET: return Type.CREEBET;
            case WANDERER: return Type.WANDERER;
            case GRAHAM: return Type.GRAHAM;
            case MATCH: return Type.MATCH;
            case BUST: return Type.BUST;
            case STAGE: return Type.STAGE;
            case VOID: return Type.VOID;
            case SKULL_AND_ROSES: return Type.SKULL_AND_ROSES;
            case FIGHTERS: return Type.FIGHTERS;
            case POINTER: return Type.POINTER;
            case PIGSCENE: return Type.PIGSCENE;
            case BURNINGSKULL: return Type.BURNING_SKULL;
            case SKELETON: return Type.SKELETON;
            case DONKEYKONG: return Type.DONKEY_KONG;
            case WITHER: return Type.WITHER;
            default:
                throw new AssertionError(art);
        }
    }
}
