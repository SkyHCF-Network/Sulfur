package net.skyhcf.sulfur.check.impl.aimassist;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.util.update.RotationUpdate;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.event.AlertType;

import org.bukkit.entity.Player;

public class AimAssistC extends RotationCheck {

    public AimAssistC(PlayerData playerData) {
        super(playerData, "Aim Type C");
    }

    @Override
    public void handleCheck( Player player,  RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }

         float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        double vl = this.getVl();

        AlertData[] alertData = new AlertData[]{
                new AlertData("Y", diffYaw),
                new AlertData("VL", vl)
        };

        if (update.getFrom().getPitch() == update.getTo().getPitch() && diffYaw >= 3.0f && update.getFrom().getPitch() != 90.0f && update.getTo().getPitch() != 90.0f) {
            if ((vl += 0.9) >= 6.3) {
                this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
            }
        } else {
            vl -= 1.6;
        }

        this.setVl(vl);
    }

}
