package net.skyhcf.sulfur.check.impl.inventory;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInWindowClick;
import org.bukkit.entity.Player;

public class InventoryE extends PacketCheck {

	private boolean sent;

	public InventoryE(PlayerData playerData) {
		super(playerData, "Inventory Type E");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInWindowClick) {
			if (this.sent) {
				this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], true);
			}
		} else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand) packet).c() == EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
			this.sent = true;
		} else if (packet instanceof PacketPlayInFlying) {
			this.sent = false;
		}
	}

}
