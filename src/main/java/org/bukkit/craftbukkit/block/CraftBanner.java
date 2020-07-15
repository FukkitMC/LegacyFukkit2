package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftBanner extends CraftBlockState implements Banner {

    private final BannerBlockEntity banner;
    private DyeColor base;
    private List<Pattern> patterns = new ArrayList<Pattern>();

    public CraftBanner(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        banner = (BannerBlockEntity) world.getTileEntityAt(getX(), getY(), getZ());

        base = DyeColor.getByDyeData((byte) banner.field_1326);

        if (banner.field_1327 != null) {
            for (int i = 0; i < banner.field_1327.size(); i++) {
                CompoundTag p = (CompoundTag) banner.field_1327.getCompound(i);
                patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInt("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    public CraftBanner(final Material material, final BannerBlockEntity te) {
        super(material);
        banner = te;

        base = DyeColor.getByDyeData((byte) banner.field_1326);

        if (banner.field_1327 != null) {
            for (int i = 0; i < banner.field_1327.size(); i++) {
                CompoundTag p = (CompoundTag) banner.field_1327.getCompound(i);
                patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInt("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return this.patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return this.patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        this.patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            banner.field_1326 = base.getDyeData();

            ListTag newPatterns = new ListTag();

            for (Pattern p : patterns) {
                CompoundTag compound = new CompoundTag();
                compound.putInt("Color", p.getColor().getDyeData());
                compound.putString("Pattern", p.getPattern().getIdentifier());
                newPatterns.add(compound);
            }

            banner.field_1327 = newPatterns;

            banner.markDirty();
        }

        return result;
    }
}
