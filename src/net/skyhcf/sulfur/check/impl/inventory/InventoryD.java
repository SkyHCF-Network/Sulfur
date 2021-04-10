package net.skyhcf.sulfur.check.impl.inventory;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInLook;
import org.bukkit.entity.Player;

public class InventoryD extends PacketCheck {

	private int stage;

	public InventoryD(PlayerData playerData) {
		super(playerData, "Inventory Type D");
		this.stage = 0;
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (this.stage == 0) {
			if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand) packet).c() == EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
				++this.stage;
			}
		} else if (this.stage == 1) {
			if (packet instanceof PacketPlayInLook) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (this.stage == 2) {
			if (packet instanceof PacketPlayInLook) {
				this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
			}
			this.stage = 0;
		}
	}

}
