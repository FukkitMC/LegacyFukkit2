package org.bukkit.craftbukkit;

import org.bukkit.Achievement;
import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType.class_1747;
import net.minecraft.item.Item;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;
    private static final BiMap<String, org.bukkit.Achievement> achievements;

    static {
        ImmutableMap<String, org.bukkit.Achievement> specialCases = ImmutableMap.<String, org.bukkit.Achievement> builder()
                .put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH)
                .put("achievement.diamonds", Achievement.GET_DIAMONDS)
                .put("achievement.portal", Achievement.NETHER_PORTAL)
                .put("achievement.ghast", Achievement.GHAST_RETURN)
                .put("achievement.theEnd", Achievement.END_PORTAL)
                .put("achievement.theEnd2", Achievement.THE_END)
                .put("achievement.blazeRod", Achievement.GET_BLAZE_ROD)
                .put("achievement.potion", Achievement.BREW_POTION)
                .build();
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        ImmutableBiMap.Builder<String, org.bukkit.Achievement> achievementBuilder = ImmutableBiMap.<String, org.bukkit.Achievement>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }
        for (Achievement achievement : Achievement.values()) {
            if (specialCases.values().contains(achievement)) {
                continue;
            }
            achievementBuilder.put("achievement." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
        }

        achievementBuilder.putAll(specialCases);

        statistics = statisticBuilder.build();
        achievements = achievementBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Achievement getBukkitAchievement(net.minecraft.achievement.Achievement achievement) {
        return getBukkitAchievementByName(achievement.name);
    }

    public static org.bukkit.Achievement getBukkitAchievementByName(String name) {
        return achievements.get(name);
    }

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.stat.Stat statistic) {
        return getBukkitStatisticByName(statistic.name);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem")) {
            name = "stat.craftItem";
        }
        return statistics.get(name);
    }

    public static net.minecraft.stat.Stat getNMSStatistic(org.bukkit.Statistic statistic) {
        return Stats.method_6386(statistics.inverse().get(statistic));
    }

    public static net.minecraft.achievement.Achievement getNMSAchievement(org.bukkit.Achievement achievement) {
        return (net.minecraft.achievement.Achievement) Stats.method_6386(achievements.inverse().get(achievement));
    }

    public static net.minecraft.stat.Stat getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return Stats.BLOCK_STATS[material.getId()];
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return Stats.CRAFTING_STATS[material.getId()];
            }
            if (stat == Statistic.USE_ITEM) {
                return Stats.USE_STATS[material.getId()];
            }
            if (stat == Statistic.BREAK_ITEM) {
                return Stats.BREAK_STATS[material.getId()];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.stat.Stat getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        class_1747 monsteregginfo = (class_1747) net.minecraft.entity.EntityType.field_7410.get(Integer.valueOf(entity.getTypeId()));

        if (monsteregginfo != null) {
            return monsteregginfo.field_7420;
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.stat.Stat statistic) {
        String statisticString = statistic.name;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(net.minecraft.stat.Stat statistic) {
        String statisticString = statistic.name;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = (Item) Item.REGISTRY.get(new Identifier(val));
        if (item != null) {
            return Material.getMaterial(Item.getRawId(item));
        }
        Block block = (Block) Block.REGISTRY.get(new Identifier(val));
        if (block != null) {
            return Material.getMaterial(Block.getIdByBlock(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
