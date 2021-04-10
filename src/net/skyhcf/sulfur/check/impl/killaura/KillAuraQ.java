package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraQ extends PacketCheck {

	private boolean sentAttack;
	private boolean sentInteract;

	public KillAuraQ(PlayerData playerData) {
		super(playerData, "Kill Aura Type Q");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInBlockPlace) {
			if (this.sentAttack && !this.sentInteract && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 2) {
					this.ban(player);
				}
			}
		} else if (packet instanceof PacketPlayInUseEntity) {
			 EnumEntityUseAction action = ((PacketPlayInUseEntity) packet).c();
			if (action == EnumEntityUseAction.ATTACK) {
				this.sentAttack = true;
			} else if (action == EnumEntityUseAction.INTERACT) {
				this.sentInteract = true;
			}
		} else if (packet instanceof PacketPlayInFlying) {
			 boolean b = false;
			this.sentInteract = false;
			this.sentAttack = false;
		}
	}

}
