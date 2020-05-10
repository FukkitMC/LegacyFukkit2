package io.github.fukkitmc.legacy.extra;

import net.minecraft.scoreboard.AbstractTeam;

public interface ScoreboardTeamExtra {

    boolean canSeeFriendlyInvisibles();

    AbstractTeam.VisibilityRule getNameTagVisibility();

}
