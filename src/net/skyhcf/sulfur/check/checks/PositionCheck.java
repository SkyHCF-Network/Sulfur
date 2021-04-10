package net.skyhcf.sulfur.check.checks;

import net.skyhcf.sulfur.check.AbstractCheck;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import net.skyhcf.sulfur.player.PlayerData;

public abstract class PositionCheck extends AbstractCheck<PositionUpdate> {

    public PositionCheck(PlayerData playerData, String name) {
        super(playerData, PositionUpdate.class, name);
    }

}
