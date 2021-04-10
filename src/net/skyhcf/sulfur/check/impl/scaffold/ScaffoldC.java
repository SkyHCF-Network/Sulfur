package net.skyhcf.sulfur.check.impl.scaffold;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayInLook;
import net.minecraft.server.v1_7_R4.PacketPlayInPosition;
import net.minecraft.server.v1_7_R4.PacketPlayInPositionLook;
import org.bukkit.entity.Player;

public class ScaffoldC extends PacketCheck {

	private int looks;
	private int stage;

	public ScaffoldC(PlayerData playerData) {
		super(playerData, "Scaffold Type C");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		double vl = this.getVl();

		if (packet instanceof PacketPlayInLook) {
			if (this.stage == 0) {
				++this.stage;
			} else if (this.stage == 4) {
				if ((vl += 1.75) > 3.5) {
					AlertData[] alertData = new AlertData[]{
							new AlertData("VL", vl)
					};

					this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
				}

				this.stage = 0;
			} else {
				this.looks = 0;
				this.stage = 0;
				vl -= 0.2;
			}
		} else if (packet instanceof PacketPlayInBlockPlace) {
			if (this.stage == 1) {
				++this.stage;
			} else {
				this.looks = 0;
				this.stage = 0;
			}
		} else if (packet instanceof PacketPlayInArmAnimation) {
			if (this.stage == 2) {
				++this.stage;
			} else {
				this.looks = 0;
				this.stage = 0;
				vl -= 0.2;
			}
		} else if (packet instanceof PacketPlayInPositionLook || packet instanceof PacketPlayInPosition) {
			if (this.stage == 3) {
				if (++this.looks == 3) {
					this.stage = 4;
					this.looks = 0;
				}
			} else {
				this.looks = 0;
				this.stage = 0;
			}
		}

		this.setVl(vl);
	}

}
