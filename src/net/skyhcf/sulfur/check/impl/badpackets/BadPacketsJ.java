package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import org.bukkit.entity.Player;

public class BadPacketsJ extends PacketCheck {

	private boolean placing;

	public BadPacketsJ(PlayerData playerData) {
		super(playerData, "Invalid Packets Type J");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInBlockDig) {
			if (((PacketPlayInBlockDig) packet).g() == BadPacketsJ.RELEASE_USE_ITEM) {
				if (!this.placing && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
					 int violations = this.playerData.getViolations(this, 60000L);
					if (!this.playerData.isBanning() && violations > 2) {
						this.ban(player);
					}
				}
				this.placing = false;
			}
		} else if (packet instanceof PacketPlayInBlockPlace) {
			this.placing = true;
		}
	}

}
