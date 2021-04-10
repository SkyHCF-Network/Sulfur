package net.skyhcf.sulfur.check.impl.aimassist;

import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.update.RotationUpdate;

public class AimAssistJ extends RotationCheck {

    public AimAssistJ(PlayerData playerData) {
        super(playerData, "Aim Type J");
    }

    private int verbose;
    private double lastDeltaPitch;

    @Override
    public void handleCheck(Player player, RotationUpdate update) {
        double pitch = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch());

        double deltaPitch = this.lastDeltaPitch;
        this.lastDeltaPitch = pitch;

        double pitchAcceleration = Math.abs(pitch-deltaPitch);

        double offset = Math.pow(2.0, 24.0);
        double gcd = gcd((long)(pitch * offset), (long)(deltaPitch * offset));
        double simple = gcd / offset;

        double magic = pitch % simple;

        if(pitch > 0 && magic > 1E-4 && pitchAcceleration > 2 && simple < 0.006 && simple > 0){
            if(verbose++ > 3){
                AlertData[] data = new AlertData[]{
                        new AlertData("GCD", simple),
                        new AlertData("PA", pitchAcceleration),
                        new AlertData("M", magic)
                };
                alert(player, AlertType.EXPERIMENTAL, data, true);
            }
        }else{
            verbose = 0;
        }

    }

    private long gcd(long a, long b) {
        if (b <= 0x4000) {
            return a;
        }
        return gcd(b, a % b);
    }

}