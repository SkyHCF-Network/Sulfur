package net.skyhcf.sulfur.check.impl.aimassist;

import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.Verbose;
import net.skyhcf.sulfur.util.update.RotationUpdate;

public class AimAssistH extends RotationCheck {

   private final Verbose verbose = new Verbose();

    public AimAssistH(PlayerData playerData) {
        super(playerData, "Aim Type H");
    }

    @Override
    public void handleCheck(Player player, RotationUpdate update) {

        double pitch = Math.abs(update.getTo().getPitch()-update.getFrom().getPitch());
        double yaw = Math.abs(update.getTo().getYaw()-update.getFrom().getYaw());

        if(pitch > 0 || yaw > 0){
            playerData.hasLooked = true;
            double offset = pitch % 1;
            double value = pitch % offset;

            double offset1 = yaw % 1;
            double value1 = yaw % offset1;

            if(value == 0 && pitch < 0.1 && pitch > 0 && value1 < 0.1 && yaw > 1.4){
                AlertData[] data = new AlertData[]{
                        new AlertData("P", pitch),
                        new AlertData("Y", yaw),
                        new AlertData("V", value),
                        new AlertData("V2", value1)
                };
                if(verbose.flag(2, 550))alert(player, AlertType.RELEASE, data, true);
            }
        }else{
            playerData.hasLooked = false;
        }

    }

}