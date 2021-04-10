package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.MathUtil;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;

import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerF extends PacketCheck {

	private Deque<Integer> recentData = new LinkedList<>();
	private int movements;
	private double lastStdev;

	public AutoClickerF(PlayerData playerData) {
		super(playerData, "Auto Clicker Type F");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			if (this.movements < 10 && (this.playerData.isDigging() && !this.playerData.isPlacing())) {
				this.recentData.add(this.movements);
				if (this.recentData.size() == 50) {
					double stdDev = MathUtil.stDeviation(this.recentData);

					int vl = (int) this.getVl();
					if (stdDev < 0.7) {
						double diffStdev = Math.abs(stdDev - lastStdev);

						if (diffStdev < 0.025) {
							if (++vl >= 5) {
								AlertData[] alertData = new AlertData[]{
										new AlertData("DSTDEV", diffStdev),
										new AlertData("CPS", 1000.0D / (MathUtil.average(this.recentData) * 50.0D)),
								};
										this.alert(player, AlertType.RELEASE, alertData, true);
								}
							}
						} else {
						vl--;
					}

					lastStdev = stdDev;
					this.setVl((double) vl);
					this.recentData.clear();
					}
				}
				this.movements = 0;
			} else if (packet instanceof  PacketPlayInFlying) {
			this.movements++;
		}
	}
}
