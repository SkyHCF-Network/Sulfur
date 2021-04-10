package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInEntityAction;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class BadPacketsD extends PacketCheck {

	private boolean sent;

	public BadPacketsD(PlayerData playerData) {
		super(playerData, "Invalid Packets Type D");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInEntityAction) {
			 int action = ((PacketPlayInEntityAction) packet).d();
			if (action == BadPacketsD.START_SNEAKING || action == BadPacketsD.STOP_SNEAKING) {
				if (this.sent) {
					if (this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
						 int violations = this.playerData.getViolations(this, 60000L);
						if (!this.playerData.isBanning() && violations > 2) {
							this.ban(player);
						}
					}
				} else {
					this.sent = true;
				}
			}
		} else if (packet instanceof PacketPlayInFlying) {
			this.sent = false;
		}
	}

}
