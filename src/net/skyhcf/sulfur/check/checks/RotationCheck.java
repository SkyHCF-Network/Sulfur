package net.skyhcf.sulfur.check.checks;

import net.skyhcf.sulfur.check.AbstractCheck;
import net.skyhcf.sulfur.util.update.RotationUpdate;
import net.skyhcf.sulfur.player.PlayerData;

public abstract class RotationCheck extends AbstractCheck<RotationUpdate> {

    public RotationCheck(PlayerData playerData, String name) {
        super(playerData, RotationUpdate.class, name);
    }


}
