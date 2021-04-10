package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerI extends PacketCheck {

	private  Deque<Integer> recentCounts;
	private int flyingCount;

	public AutoClickerI(PlayerData playerData) {
		super(playerData, "Auto Clicker Type I");
		this.recentCounts = new LinkedList<>();
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig) packet).g() == AutoClickerI.RELEASE_USE_ITEM) {
			if (this.flyingCount < 10 && this.playerData.getLastAnimationPacket() + 2000L > System.currentTimeMillis()) {
				this.recentCounts.add(this.flyingCount);

				if (this.recentCounts.size() == 100) {
					double average = 0.0;

					for ( double flyingCount : this.recentCounts) {
						average += flyingCount;
					}

					average /= this.recentCounts.size();

					double stdDev = 0.0;

					for ( long l : this.recentCounts) {
						stdDev += Math.pow(l - average, 2.0);
					}

					stdDev /= this.recentCounts.size();
					stdDev = Math.sqrt(stdDev);

					double vl = this.getVl();

					if (stdDev < 0.2) {
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
		} else if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace) packet).getItemStack() != null && ((PacketPlayInBlockPlace) packet).getItemStack().getName().toLowerCase().contains("sword")) {
			this.flyingCount = 0;
		} else if (packet instanceof PacketPlayInFlying) {
			++this.flyingCount;
		}
	}

}
