package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.MathUtil;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

import java.util.*;

public class AutoClickerE extends PacketCheck {

	private Deque<Integer> recentData = new LinkedList<>();
	private int movements;

	public AutoClickerE(PlayerData playerData) {
		super(playerData, "Auto Clicker Type E");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			if (this.movements < 10 && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
				this.recentData.add(this.movements);
				if (this.recentData.size() == 500) {
					int outliers = Math.toIntExact(recentData.stream()
							.mapToInt(i -> 1)
							.filter(i -> i > 3)
							.count());

					double vl = this.getVl();
					if (outliers <= 2) {
						if ((vl += 1.4) >= 3.2) {
							AlertData[] alertData = new AlertData[]{
									new AlertData("O", outliers),
									new AlertData("VL", vl),
									new AlertData("CPS", 1000.0D / (MathUtil.average(this.recentData) * 50.0D))
							};
							this.alert(player, AlertType.RELEASE, alertData, true);
						}
					} else {
						vl -= 0.65;
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

