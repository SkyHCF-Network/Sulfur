package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInSteerVehicle;
import org.bukkit.entity.Player;

public class BadPacketsI extends PacketCheck {

	private float lastYaw;
	private float lastPitch;
	private boolean ignore;

	public BadPacketsI(PlayerData playerData) {
		super(playerData, "Invalid Packets Type I");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInFlying) {
			 PacketPlayInFlying flying = (PacketPlayInFlying) packet;
			if (!flying.j() && flying.k()) {
				if (this.lastYaw == flying.g() && this.lastPitch == flying.h()) {
					if (!this.ignore) {
						this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
					}

					this.ignore = false;
				}

				this.lastYaw = flying.g();
				this.lastPitch = flying.h();
			} else {
				this.ignore = true;
			}
		} else if (packet instanceof PacketPlayInSteerVehicle) {
			this.ignore = true;
		}
	}

}
