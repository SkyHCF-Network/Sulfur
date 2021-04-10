package net.skyhcf.sulfur.check.impl.fly;

import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

public class FlyE extends PositionCheck {

    private int verbose;

    public FlyE(PlayerData playerData) {
        super(playerData, "Flight Type E");
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
                || player.getAllowFlight() || this.playerData.isInLiquid()
                || !this.playerData.isWasUnderBlock()) {
            verbose = 0;
        }else {
            if(playerData.getAirTicks() > 6 && motionY >= 0){
                if(verbose++ > 3){
                    AlertData[] alertData = new AlertData[]{
                            new AlertData("MotionY ", motionY),
                    };
                    alert(player, AlertType.EXPERIMENTAL, alertData, true);
                }
            }
            
        }
    }
}
