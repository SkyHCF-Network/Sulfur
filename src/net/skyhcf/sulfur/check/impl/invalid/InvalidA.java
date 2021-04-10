package net.skyhcf.sulfur.check.impl.invalid;

import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.update.PositionUpdate;

public class InvalidA extends PositionCheck {

    private int verbose;

    public InvalidA(PlayerData playerData) {
        super(playerData, "Invalid Type A");
    }

    @Override
    public void handleCheck(Player player, PositionUpdate type) {
        double Y = Math.abs(type.getTo().getY()-type.getFrom().getY());

        if (Y > 0.39 && playerData.getAirTicks() == 0 && playerData.getGroundTicks() > 5
                && !playerData.isOnStairs()) {

            if (verbose++ > 3) {
                AlertData[] data = new AlertData[]{
                        new AlertData("Y", Y)
                };
                alert(player, AlertType.RELEASE, data, true);
            }
        } else verbose = Math.max(0, verbose - 1);
    }
}
