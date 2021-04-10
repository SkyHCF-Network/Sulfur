package net.skyhcf.sulfur.check.impl.aimassist;

import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.update.RotationUpdate;

public class AimAssistF extends RotationCheck {


    public AimAssistF(PlayerData playerData) {
        super(playerData, "Aim Type F");
    }

    @Override
    public void handleCheck(Player player, RotationUpdate update) {
        float fromYaw = (update.getFrom().getYaw() - 90) % 360F;
        float toYaw = (update.getTo().getYaw() - 90) % 360F;


        if (fromYaw < 0F)
            fromYaw += 360F;

        if (toYaw < 0F)
            toYaw += 360F;

        double diffYaw = Math.abs(toYaw - fromYaw);

        int vl = (int) this.getVl();

        if (diffYaw > 0D) {
            if (diffYaw % 1 == 0D) {
                if ((vl += 12) > 45) {
                    AlertData[] alertData = new AlertData[]{
                            new AlertData("VL", vl),
                    };
                    this.alert(player, AlertType.RELEASE, alertData, true);
                }
            } else {
                vl -= 8;
            }
        } else {
            vl -= 8;
            }
        this.setVl(vl);
        }

    }
