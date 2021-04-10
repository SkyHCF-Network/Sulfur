package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraM extends PacketCheck {

	private int swings;
	private int attacks;

	public KillAuraM(PlayerData playerData) {
		super(playerData, "Kill Aura Type M");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (!this.playerData.isDigging() && !this.playerData.isPlacing()) {
			if (packet instanceof PacketPlayInFlying) {
				if (this.attacks > 0 && this.swings > this.attacks) {
					AlertData[] alertData = new AlertData[]{
							new AlertData("S", this.swings),
							new AlertData("A", this.attacks)
					};

					this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
				}
				 KillAuraN auraN = this.playerData.getCheck(KillAuraN.class);
				if (auraN != null) {
					auraN.handleCheck(player, new int[]{ this.swings, this.attacks });
				}
				this.swings = 0;
				this.attacks = 0;
			} else if (packet instanceof PacketPlayInArmAnimation) {
				++this.swings;
			} else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
				++this.attacks;
			}
		}
	}

}
