package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraS extends PacketCheck {

	private boolean sentArmAnimation;
	private boolean sentAttack;
	private boolean sentBlockPlace;
	private boolean sentUseEntity;

	public KillAuraS(PlayerData playerData) {
		super(playerData, "Kill Aura Type S");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			this.sentArmAnimation = true;
		} else if (packet instanceof PacketPlayInUseEntity) {
			if (((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
				this.sentAttack = true;
			} else {
				this.sentUseEntity = true;
			}
		} else if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace) packet).getItemStack() != null && ((PacketPlayInBlockPlace) packet).getItemStack().getName().toLowerCase().contains("sword")) {
			this.sentBlockPlace = true;
		} else if (packet instanceof PacketPlayInFlying) {
			if (this.sentArmAnimation && !this.sentAttack && this.sentBlockPlace && this.sentUseEntity && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 2) {
					this.ban(player);
				}
			}
			 boolean b = false;
			this.sentUseEntity = false;
			this.sentBlockPlace = false;
			this.sentAttack = false;
			this.sentArmAnimation = false;
		}
	}

}
