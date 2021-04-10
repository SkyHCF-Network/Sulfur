package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerH extends PacketCheck {

	private  Deque<Integer> recentCounts;
	private int flyingCount;
	private boolean release;

	public AutoClickerH(PlayerData playerData) {
		super(playerData, "Auto Clicker Type H");
		this.recentCounts = new LinkedList<>();
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isDigging() && !this.playerData.isPlacing() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L &&
		    this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.playerData.isFakeDigging()) {

			if (this.flyingCount < 10) {
				if (this.release) {
					this.release = false;
					this.flyingCount = 0;
					return;
				}

				this.recentCounts.add(this.flyingCount);

				if (this.recentCounts.size() == 100) {
					double average = 0.0;

					for ( int i : this.recentCounts) {
						average += i;
					}

					average /= this.recentCounts.size();

					double stdDev = 0.0;

					for ( int j : this.recentCounts) {
						stdDev += Math.pow(j - average, 2.0);
					}

					stdDev /= this.recentCounts.size();
					stdDev = Math.sqrt(stdDev);

					double vl = this.getVl();

					if (stdDev < 0.45) {
						if ((vl += 1.4) >= 4.0) {
							AlertData[] alertData = new AlertData[]{
									new AlertData("STD", stdDev),
									new AlertData("VL", vl)
							};

							this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
						}
					} else {
						vl -= 0.8;
					}

					this.setVl(vl);
					this.recentCounts.clear();
				}
			}

			this.flyingCount = 0;
		} else if (packet instanceof PacketPlayInFlying) {
			++this.flyingCount;
		} else if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig) packet).g() == AutoClickerH.RELEASE_USE_ITEM) {
			this.release = true;
		}
	}

}
