package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInCloseWindow;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraO extends PacketCheck {

	private boolean sent;

	public KillAuraO(PlayerData playerData) {
		super(playerData, "Kill Aura Type O");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
			if (this.sent && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 2) {
					this.ban(player);
				}
			}
		} else if (packet instanceof PacketPlayInCloseWindow) {
			this.sent = true;
		} else if (packet instanceof PacketPlayInFlying) {
			this.sent = false;
		}
	}

}
