package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.MathUtil;
import net.frozenorb.qlib.util.LinkedList;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

import java.util.Deque;

public class AutoClickerD extends PacketCheck
{
	private Deque<Integer> recentData = new LinkedList<>();

	private int movements;

	public AutoClickerD(PlayerData playerData) {
		super(playerData, "Auto Clicker Type D");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			if (this.movements < 10 && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
				this.recentData.add(this.movements);
				if (this.recentData.size() == 250) {
					double stdDev = MathUtil.stDeviation(this.recentData);

					double vl = this.getVl();
					if (stdDev < 0.55) {
						if (++vl > 4) {
							AlertData[] alertData = new AlertData[]{
									new AlertData("STD", stdDev),
									new AlertData("CPS",  1000.0D / ((MathUtil.average(this.recentData)) * 50.0D)),
									new AlertData("VL", vl),
							};
							this.alert(player, AlertType.RELEASE, alertData, false);
						}
					} else {
						vl -= 2.4;
					}

					this.setVl(vl);

					this.recentData.clear();
				}
			}
			this.movements = 0;
		} else if (packet instanceof PacketPlayInFlying) {
			this.movements++;
		}
	}
}
