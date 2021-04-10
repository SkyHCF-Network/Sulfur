package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInPosition;
import net.minecraft.server.v1_7_R4.PacketPlayInPositionLook;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraK extends PacketCheck {

	private int ticksSinceStage;
	private int streak;
	private int stage;

	public KillAuraK(PlayerData playerData) {
		super(playerData, "Kill Aura Type K");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			if (this.stage == 0) {
				this.stage = 1;
			} else {
				 boolean b = false;
				this.stage = 0;
				this.streak = 0;
			}
		} else if (packet instanceof PacketPlayInUseEntity) {
			if (this.stage == 1) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (packet instanceof PacketPlayInPositionLook) {
			if (this.stage == 2) {
				++this.stage;
			} else {
				this.stage = 0;
			}
		} else if (packet instanceof PacketPlayInPosition) {
			if (this.stage == 3) {
				if (++this.streak >= 15) {
					AlertData[] alertData = new AlertData[]{
							new AlertData("STR", this.streak)
					};

					this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
				}
				this.ticksSinceStage = 0;
			}
			this.stage = 0;
		}
		if (packet instanceof PacketPlayInFlying && ++this.ticksSinceStage > 40) {
			this.streak = 0;
		}
	}

}
