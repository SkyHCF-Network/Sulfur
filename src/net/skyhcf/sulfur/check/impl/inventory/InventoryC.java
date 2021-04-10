package net.skyhcf.sulfur.check.impl.inventory;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.CustomLocation;
import net.skyhcf.sulfur.player.PlayerData;

import java.util.Deque;
import java.util.LinkedList;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInWindowClick;
import org.bukkit.entity.Player;

public class InventoryC extends PacketCheck {

	private  Deque<Long> delays;

	public InventoryC(PlayerData playerData) {
		super(playerData, "Inventory Type C");

		this.delays = new LinkedList<>();
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInWindowClick && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
			 CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
			if (lastMovePacket == null) {
				return;
			}
			 long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
			this.delays.add(delay);
			if (this.delays.size() == 10) {
				double average = 0.0;
				for ( long loopDelay : this.delays) {
					average += loopDelay;
				}
				average /= this.delays.size();
				this.delays.clear();
				double vl = this.getVl();
				if (average <= 35.0) {
					if ((vl += 1.25) >= 4.0) {
						AlertData[] alertData = new AlertData[]{
								new AlertData("AVG", average),
								new AlertData("VL", vl)
						};

						if (this.alert(player, AlertType.RELEASE, alertData, true)) {
						} else {
							vl = 0.0;
						}
					}
				} else {
					vl -= 0.5;
				}
				this.setVl(vl);
			}
		}
	}

}
