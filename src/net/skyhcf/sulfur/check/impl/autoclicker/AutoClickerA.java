package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerA extends PacketCheck {

	private int swings;
	private int movements;

	public AutoClickerA( PlayerData playerData) {
		super(playerData, "Auto Clicker Type A");
	}

	@Override
	public void handleCheck( Player player,  Packet packet) {
			if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
				++this.swings;
			}
			else if (packet instanceof PacketPlayInFlying && this.swings > 0 && ++this.movements == 20) {
				AlertData[] alertData = new AlertData[]
						{ new AlertData("CPS", this.swings) };

				if (this.swings > 20 && this.alert(player, AlertType.RELEASE, alertData, true)) {
					int violations = this.playerData.getViolations(this, 60000L);

					if (!this.playerData.isBanning() && violations > 15) {
						this.ban(player);
					}
				}
				int n = 0;
				this.swings = n;
				this.movements = n;
			}
			playerData.setLastCps(this.swings);
		}
	}
