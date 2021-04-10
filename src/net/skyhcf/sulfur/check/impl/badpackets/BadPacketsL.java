package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayInEntityAction;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInSteerVehicle;
import org.bukkit.entity.Player;

public class BadPacketsL extends PacketCheck {

	private boolean sent;
	private boolean vehicle;

	public BadPacketsL(PlayerData playerData) {
		super(playerData, "Invalid Packets Type L");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInFlying) {
			if (this.sent) {
				this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
			}
			this.vehicle = false;
			this.sent = false;

		} else if (packet instanceof PacketPlayInBlockPlace) {
			PacketPlayInBlockPlace blockPlace = (PacketPlayInBlockPlace) packet;
			if (blockPlace.getFace() == 255) {
				ItemStack itemStack = blockPlace.getItemStack();
				if (itemStack != null && itemStack.getName().toLowerCase().contains("sword") && this.playerData.isSprinting() && !this.vehicle) {
					this.sent = true;
				}
			}
		} else if (packet instanceof PacketPlayInEntityAction &&  ((PacketPlayInEntityAction) packet).d() == BadPacketsL.STOP_SPRINTING) {
			this.sent = false;
			} else if (packet instanceof  PacketPlayInSteerVehicle) {
			this.vehicle = true;
		}
		}
	}

