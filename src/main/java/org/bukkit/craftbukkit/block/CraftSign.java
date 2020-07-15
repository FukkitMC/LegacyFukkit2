package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftSign extends CraftBlockState implements Sign {
    private final SignBlockEntity sign;
    private final String[] lines;

    public CraftSign(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        sign = (SignBlockEntity) world.getTileEntityAt(getX(), getY(), getZ());
        // Spigot start
        if (sign == null) {
            lines = new String[]{"", "", "", ""};
            return;
        }
        // Spigot end
        lines = new String[sign.field_1460.length];
        System.arraycopy(revertComponents(sign.field_1460), 0, lines, 0, lines.length);
    }

    public CraftSign(final Material material, final SignBlockEntity te) {
        super(material);
        sign = te;
        lines = new String[sign.field_1460.length];
        System.arraycopy(revertComponents(sign.field_1460), 0, lines, 0, lines.length);
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            Text[] newLines = sanitizeLines(lines);
            System.arraycopy(newLines, 0, sign.field_1460, 0, 4);
            sign.markDirty();
        }

        return result;
    }

    public static Text[] sanitizeLines(String[] lines) {
        Text[] components = new Text[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = new LiteralText("");
            }
        }

        return components;
    }

    public static String[] revertComponents(Text[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(Text component) {
        return CraftChatMessage.fromComponent(component);
    }

    @Override
    public SignBlockEntity getTileEntity() {
        return sign;
    }
}
