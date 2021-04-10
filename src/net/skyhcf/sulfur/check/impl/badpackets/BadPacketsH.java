package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInHeldItemSlot;
import org.bukkit.entity.Player;

public class BadPacketsH extends PacketCheck {

	private int lastSlot;

	public BadPacketsH(PlayerData playerData) {
		super(playerData, "Invalid Packets Type H");

		this.lastSlot = -1;
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInHeldItemSlot) {
			 int slot = ((PacketPlayInHeldItemSlot) packet).c();
			if (this.lastSlot == slot && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 2) {
					this.ban(player);
				}
			}
			this.lastSlot = slot;
		}
	}

}
