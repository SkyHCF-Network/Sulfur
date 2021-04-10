package net.skyhcf.sulfur.check.impl.inventory;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInWindowClick;
import org.bukkit.entity.Player;

public class InventoryA extends PacketCheck {

	public InventoryA(PlayerData playerData) {
		super(playerData, "Inventory Type A");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInWindowClick && ((PacketPlayInWindowClick) packet).c() == 0 && !this.playerData.isInventoryOpen()) {
			if (this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 5) {
					this.ban(player);
				}
			}
			this.playerData.setInventoryOpen(true);
		}
	}

}
