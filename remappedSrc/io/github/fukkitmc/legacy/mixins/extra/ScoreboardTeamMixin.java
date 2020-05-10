package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.ScoreboardTeamExtra;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Team.class)
public class ScoreboardTeamMixin implements ScoreboardTeamExtra {
    @Override
    public boolean canSeeFriendlyInvisibles() {
        return false;
    }

    @Override
    public AbstractTeam.VisibilityRule getNameTagVisibility() {
        return null;
    }
}
