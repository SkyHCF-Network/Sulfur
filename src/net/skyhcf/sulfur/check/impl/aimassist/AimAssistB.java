package net.skyhcf.sulfur.check.impl.aimassist;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.util.update.RotationUpdate;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.event.AlertType;
import org.bukkit.entity.Player;

public class AimAssistB extends RotationCheck {

    private float suspiciousYaw;
    
    public AimAssistB(PlayerData playerData) {
        super(playerData, "Aim Type B");
    }
    
    @Override
    public void handleCheck(Player player, RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }

         float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());

        if (diffYaw > 1.0f && Math.round(diffYaw * 10.0f) * 0.1f == diffYaw && Math.round(diffYaw) != diffYaw && diffYaw % 1.5f != 0.0f) {
            AlertData[] alertData = new AlertData[]{
                    new AlertData("Y", diffYaw)
            };

            if (diffYaw == this.suspiciousYaw && this.alert(player, AlertType.RELEASE, alertData, true)) {
                 int violations = this.playerData.getViolations(this, 60000L);

                if (!this.playerData.isBanning() && violations > 20) {
                    this.ban(player);
                }
            }

            this.suspiciousYaw = Math.round(diffYaw * 10.0f) * 0.1f;
        } else {
            this.suspiciousYaw = 0.0f;
        }
    }

}
