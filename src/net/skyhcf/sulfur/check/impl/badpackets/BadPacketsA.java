package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInSteerVehicle;
import org.bukkit.entity.Player;

public class BadPacketsA extends PacketCheck {

	private int streak;

	public BadPacketsA(PlayerData playerData) {
		super(playerData, "Invalid Packets Type A");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInFlying) {
			if (((PacketPlayInFlying) packet).j()) {
				this.streak = 0;
			} else if (++this.streak > 20 && this.alert(player, AlertType.RELEASE, new AlertData[0], false) && !this.playerData.isBanning()) {
				this.ban(player);

			}
		} else if (packet instanceof PacketPlayInSteerVehicle) {
			this.streak = 0;
		}
		else if (packet instanceof PacketPlayOutAttachEntity)
			this.streak = 0;
	}

}
