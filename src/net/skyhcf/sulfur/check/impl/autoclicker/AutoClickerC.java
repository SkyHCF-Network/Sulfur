package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.frozenorb.qlib.util.LinkedList;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import org.bukkit.entity.Player;

import java.util.Deque;

public class AutoClickerC extends PacketCheck {

	private Deque<Integer> recentData = new LinkedList<>();

	private int movements;

	public AutoClickerC(PlayerData playerData) {
		super(playerData, "Auto Clicker Type C");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			if (this.movements < 10 && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
				this.recentData.add(this.movements);
				if (this.recentData.size() == 500) {
					int outliers = Math.toIntExact(recentData.stream()
							.mapToInt(i -> i)
							.filter(i -> i > 3)
							.count());

					double vl = this.getVl();
					if (outliers < 5) {
						if ((vl += 1.4) >= 4D) {
							AlertData[] alertData = new AlertData[]
									{ new AlertData("O", outliers),
										new AlertData("VL", vl), };

								this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
						}
					} else {
						vl -= 1.5;
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
