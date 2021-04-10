package net.skyhcf.sulfur.check.impl.fly;

import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

public class FlyC extends PositionCheck {

    public FlyC(PlayerData playerData) {
        super(playerData, "Flight Type C");
    }

    @Override
    public void handleCheck(Player player, PositionUpdate type) {
        double motionY = Math.abs(type.getTo().getY() - type.getFrom().getY());
        int verbose = (int)this.getVl();
        if(motionY < 0.1 && !this.playerData.isInLiquid()
                && !player.getAllowFlight()
                && !this.playerData.isOnGround()
                && this.playerData.getVelocityV() == 0) {
            if(verbose++ > 5){
                AlertData[] alertData = new AlertData[]{
                        new AlertData("MotionY ", motionY)
                };
                alert(player, AlertType.RELEASE, alertData, true);
            }
        }else verbose = Math.max(0, verbose-1);

        setVl(verbose);
    }
}
