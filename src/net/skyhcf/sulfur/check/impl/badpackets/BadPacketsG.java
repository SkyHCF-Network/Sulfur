package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInEntityAction;
import org.bukkit.entity.Player;

public class BadPacketsG extends PacketCheck {

	private int lastAction;

	public BadPacketsG(PlayerData playerData) {
		super(playerData, "Invalid Packets Type G");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInEntityAction) {
			 int playerAction = ((PacketPlayInEntityAction) packet).d();
			if (playerAction == BadPacketsG.START_SPRINTING || playerAction == BadPacketsG.STOP_SPRINTING) {
				if (this.lastAction == playerAction && this.playerData.getLastAttackPacket() + 10000L > System.currentTimeMillis() && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
					 int violations = this.playerData.getViolations(this, 60000L);
					if (!this.playerData.isBanning() && violations > 2) {
						this.ban(player);
					}
				}
				this.lastAction = playerAction;
			}
		}
	}

}
