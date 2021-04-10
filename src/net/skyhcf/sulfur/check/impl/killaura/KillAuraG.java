package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInEntityAction;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraG extends PacketCheck {

	private int stage;

	public KillAuraG(PlayerData playerData) {
		super(playerData, "Kill Aura Type G");
		this.stage = 0;
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		 int calculusStage = this.stage % 6;
		if (calculusStage == 0) {
			if (packet instanceof PacketPlayInArmAnimation) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (calculusStage == 1) {
			if (packet instanceof PacketPlayInUseEntity) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (calculusStage == 2) {
			if (packet instanceof PacketPlayInEntityAction) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (calculusStage == 3) {
			if (packet instanceof PacketPlayInFlying) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (calculusStage == 4) {
			if (packet instanceof PacketPlayInEntityAction) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (calculusStage == 5) {
			if (packet instanceof PacketPlayInFlying) {
				AlertData[] alertData = new AlertData[]{
						new AlertData("S", this.stage)
				};

				if (++this.stage >= 30 && this.alert(player, AlertType.RELEASE, alertData, true)) {
					 int violations = this.playerData.getViolations(this, 60000L);
					if (!this.playerData.isBanning() && violations > 5) {
						this.ban(player);
					}
				}
			} else {
				this.stage = 0;
			}
		}
	}
}
