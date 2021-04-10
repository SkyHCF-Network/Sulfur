package net.skyhcf.sulfur.check.impl.fly;

import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

public class FlyD extends PositionCheck {

    private boolean fallen;
    private int verbose;

    public FlyD(PlayerData playerData) {
        super(playerData, "Flight Type D");
    }

    @Override
    public void handleCheck(Player player, PositionUpdate type) {
        double motionY = type.getTo().getY()-type.getFrom().getY();

        if ((System.currentTimeMillis() - playerData.getLastTeleportTime() <= 500
                || player.getVehicle() != null
                || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 3)
                || player.isFlying()
                || player.getGameMode() != GameMode.SURVIVAL
                || player.getNoDamageTicks() >= 3)) {
            verbose = 0;
            return;
        }

        if(playerData.isOnGround()
                || System.currentTimeMillis() - playerData.getLastVelocity() < 2000
                || player.getAllowFlight()
                || this.playerData.isInLiquid()
                || !this.playerData.isWasUnderBlock()) {
            verbose = 0;
            fallen = false;
        }else {
            if(motionY < 0){
                fallen = true;
            }
            if(fallen && motionY > -0.06){
                if(verbose++ > 6){
                    AlertData[] alertData = new AlertData[]{
                            new AlertData("Fallen ", fallen),
                            new AlertData("MotionY ", motionY),
                    };
                    this.alert(player, AlertType.RELEASE, alertData, true);
                }
            }
        }
    }
}
