package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerN extends PacketCheck {

    private int ticks;
    private int clicks;
    private int oldClicks;
    private long lastClick;

    public AutoClickerN(PlayerData playerData) {
        super(playerData, "Auto Clicker Type N");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation && !playerData.isPlacing() && !playerData.isDigging()) {
            ticks = 0;
            if ((System.currentTimeMillis() - lastClick) > 250) clicks = oldClicks;
            lastClick = System.currentTimeMillis();
        }
        if (packet instanceof PacketPlayInFlying) {
            if (++ticks <= 2) {
                if (++clicks > 100) {
                    if (clicks > 220) {
                        AlertData[] alertData = new AlertData[]{
                                new AlertData("CL ", clicks),
                                new AlertData("OC ", oldClicks)
                        };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                    }
                    if (clicks > 400) {
                        AlertData[] alertData = new AlertData[]{
                                new AlertData("LC ", lastClick),
                                new AlertData("T ", ticks)
                        };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                        oldClicks = clicks;
                        clicks = 0;
                    }
                }
            } else if (ticks == 3) {
                oldClicks = clicks;
                clicks = 0;
            }
        }
    }
}
