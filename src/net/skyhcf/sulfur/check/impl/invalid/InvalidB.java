package net.skyhcf.sulfur.check.impl.invalid;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.update.PositionUpdate;

public class InvalidB extends PositionCheck {

    private double verbose;
    private boolean fallen;

    public InvalidB(PlayerData playerData) {
        super(playerData, "Invalid Type B");
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
                || !this.playerData.isWasUnderBlock()){
            verbose = 0;
            fallen = false;
        }else{
            String tags = "";

            if(motionY < -0.05){
                fallen = true;
            }

            if(motionY >= 0 && playerData.getAirTicks() > 5){
                tags += "InvalidY ";
                verbose += 0.5f;
            }

            if(fallen && motionY > -0.08 && motionY != 0){
                tags += "Glide ";
                verbose += 0.25f;
            }

            if(motionY < -0.6 && playerData.getAirTicks() <= 1){
                tags += "FastFall";
                verbose += 0.5f;
            }

            if(verbose > 1){
                AlertData[] data = new AlertData[]{
                        new AlertData("motionY", motionY),
                        new AlertData("verbose", verbose),
                        new AlertData("tags", tags)
                };
                alert(player, AlertType.EXPERIMENTAL, data, false);
            }

        }


    }
}
