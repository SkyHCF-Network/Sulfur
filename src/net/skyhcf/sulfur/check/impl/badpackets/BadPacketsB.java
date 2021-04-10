package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class BadPacketsB extends PacketCheck {

	public BadPacketsB(PlayerData playerData) {
		super(playerData, "Invalid Packets Type B");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInFlying && Math.abs(((PacketPlayInFlying) packet).h()) > 90.0f && this.alert(player, AlertType.RELEASE, new AlertData[0], false) && !this.playerData.isBanning() && !this.playerData.isRandomBan()) {
			this.randomBan(player, 200.0);
		}
	}

}
