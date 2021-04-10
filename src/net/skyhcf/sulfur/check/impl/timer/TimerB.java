package net.skyhcf.sulfur.check.impl.timer;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

public class TimerB extends PacketCheck {

    private long lastPacketTime;
    private double balance;

    public TimerB(PlayerData playerData) {
        super(playerData, "Timer Type B");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInFlying
                && !this.playerData.isAllowTeleport()) {

            long time = System.currentTimeMillis();
            long lastPacketTime = this.lastPacketTime != 0 ? this.lastPacketTime : time - 50;
            this.lastPacketTime = time;

            long rate = time - lastPacketTime;

            balance += 50;
            balance -= rate;

            if (balance >= 10.0) {
                AlertData[] alertData = new AlertData[]{
                        new AlertData("BL", balance),
                        new AlertData("R", rate),
                        new AlertData("VL", vl)
                };
                balance = 0;
                if (++vl >= 5 && this.alert(player, AlertType.EXPERIMENTAL, alertData, false)) {
                }
            }

            this.setVl(vl);
        }
    }
}
