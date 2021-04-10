package net.skyhcf.sulfur.check.impl.aimassist;

import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.util.update.RotationUpdate;

public class AimAssistG extends RotationCheck {

    private double multiplier = Math.pow(2, 24);
    private float previous;
    private double vl, streak;

    public AimAssistG(PlayerData playerData) {
        super(playerData, "Aim Type G");
    }

    @Override
    public void handleCheck(Player player, RotationUpdate update) {
        if ((System.currentTimeMillis() - this.playerData.getLastAttackPacket() >= 2000)) {
            this.setVl(0);
            vl = 0;
            streak = 0;
            return;
        }

        if (this.playerData.getTeleportTicks() > 0 || this.playerData.getRespawnTicks() > 0 || this.playerData.getStandTicks() > 0) {
            vl = 0;
            return;
        }
        float pitchChange = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());

        long a = (long) (pitchChange * multiplier);
        long b = (long) (previous * multiplier);

        long gcd = gcd(a, b);

        float magicVal = pitchChange * 100 / previous;

        if (magicVal > 60) {
            vl = Math.max(0, vl - 1);
            streak = Math.max(0, streak - 0.125);
        }

        if (pitchChange > 0.5 && pitchChange <= 20 && gcd < 0x20000) {
            if (++vl > 1) {
                ++streak;
            }
            if (streak > 6) {
                AlertData[] alertData = new AlertData[]{
                        new AlertData("GCD ", gcd),
                        new AlertData("PC ", pitchChange),
                };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        } else {
            vl = Math.max(0, vl - 1);
        }
        this.previous = pitchChange;
    }

    private long gcd(long a, long b) {
        if (b <= 0x4000) {
            return a;
        }
        return gcd(b, a % b);
    }
}