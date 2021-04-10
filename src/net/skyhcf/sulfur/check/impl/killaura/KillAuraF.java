package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraF extends PacketCheck {

	private boolean sent;

	public KillAuraF(PlayerData playerData) {
		super(playerData, "Kill Aura Type F");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInUseEntity) {
			if (this.sent && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
				 int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 2) {
					this.ban(player);
				}
			}
		} else if (packet instanceof PacketPlayInBlockDig) {
			 int digType = ((PacketPlayInBlockDig) packet).g();
			if ((digType == KillAuraF.START_DESTROY_BLOCK || digType == KillAuraF.ABORT_DESTROY_BLOCK || digType == KillAuraF.RELEASE_USE_ITEM) && !this.playerData.isInstantBreakDigging() && !this.playerData.isFakeDigging()) {
				this.sent = true;
			}
		} else if (packet instanceof PacketPlayInFlying) {
			this.sent = false;
		}
	}

}
