package net.skyhcf.sulfur.check.impl.aimassist;

import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.update.RotationUpdate;

public class AimAssistI extends RotationCheck {

    public AimAssistI(PlayerData playerData) {
        super(playerData, "Aim Type I");
    }

    @Override
    public void handleCheck(Player player, RotationUpdate update) {
        double yaw = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw()) % 360;
        double offset = yaw % Math.PI;
        double value = yaw % offset;
        double magic = yaw - (offset + value);

        if (yaw > 1400 && magic > 1000 && value < 3 && offset < 3 && System.currentTimeMillis() - playerData.getLastAttack() < 250) {
            AlertData[] data = new AlertData[]{
                    new AlertData("Y", yaw),
                    new AlertData("V", value),
                    new AlertData("M", magic),
                    new AlertData("O", offset)
            };
            alert(player, AlertType.DEVELOPMENT, data, false);
        }
    }

}