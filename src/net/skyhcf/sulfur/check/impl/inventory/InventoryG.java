package net.skyhcf.sulfur.check.impl.inventory;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInEntityAction;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInSteerVehicle;
import org.bukkit.entity.Player;

public class InventoryG extends PacketCheck {

	private boolean sent;
	private boolean vehicle;

	public InventoryG(PlayerData playerData) {
		super(playerData, "Inventory Type G");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInFlying) {
			if (this.sent) {
				this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], true);
			}
			this.vehicle = false;
			this.sent = false;
		} else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand) packet).c() == EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
			if (this.playerData.isSprinting() && !this.vehicle) {
				this.sent = true;
			}
		} else if (packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction) packet).d() == InventoryG.STOP_SPRINTING) {
			this.sent = false;
		} else if (packet instanceof PacketPlayInSteerVehicle) {
			this.vehicle = true;
		}
	}

}
