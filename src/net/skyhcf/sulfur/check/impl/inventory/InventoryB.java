package net.skyhcf.sulfur.check.impl.inventory;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInEntityAction;
import org.bukkit.entity.Player;

public class InventoryB extends PacketCheck {

	public InventoryB(PlayerData playerData) {
		super(playerData, "Inventory Type B");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (((packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction) packet).d() == InventoryB.START_SPRINTING) || packet instanceof PacketPlayInArmAnimation) && this.playerData.isInventoryOpen()) {
			if (this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 5) {
					this.ban(player);
				}
			}
			this.playerData.setInventoryOpen(false);
		}
	}

}
