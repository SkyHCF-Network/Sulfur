package net.skyhcf.sulfur.check.impl.fly;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.check.checks.PositionCheck;
import org.bukkit.entity.Player;

public class FlyB extends PositionCheck {

    public FlyB(PlayerData playerData) {
        super(playerData, "Flight Type B");
    }
    
    @Override
    public void handleCheck( Player player,  PositionUpdate update) {
        int vl = (int)this.getVl();

        if (!this.playerData.isInLiquid() && !this.playerData.isOnGround()) {
             double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
             double offsetY = update.getTo().getY() - update.getFrom().getY();

            if (offsetH > 0.0 && offsetY == 0.0) {
                AlertData[] alertData = new AlertData[]{
                        new AlertData("H", offsetH),
                        new AlertData("VL", vl)
                };

                if (++vl >= 10 && this.alert(player, AlertType.RELEASE, alertData, true)) {
                     int violations = this.playerData.getViolations(this, 60000L);

                    if (!this.playerData.isBanning() && violations > 15) {
                        this.ban(player);
                    }
                }
            } else {
                vl = 0;
            }
        } else {
            vl = 0;
        }

        this.setVl(vl);
    }

}
