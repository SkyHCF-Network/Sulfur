package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class KillAuraI extends PacketCheck {

	private boolean sent;

	public KillAuraI(PlayerData playerData) {
		super(playerData, "Kill Aura Type I");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig) packet).g() == KillAuraI.STOP_DESTROY_BLOCK) {
			if (this.sent) {
				this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
			}
		} else if (packet instanceof PacketPlayInArmAnimation) {
			this.sent = true;
		} else if (packet instanceof PacketPlayInFlying) {
			this.sent = false;
		}
	}

}
