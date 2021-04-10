package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.CustomLocation;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraE extends PacketCheck {

	private long lastAttack;
	private boolean attack;

	public KillAuraE(PlayerData playerData) {
		super(playerData, "Kill Aura Type E");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		double vl = this.getVl();
		if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L &&
		    !this.playerData.isAllowTeleport()) {
			 CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
			if (lastMovePacket == null) {
				return;
			}
			 long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
			if (delay <= 25.0) {
				this.lastAttack = System.currentTimeMillis();
				this.attack = true;
			} else {
				vl -= 0.25;
			}
		} else if (packet instanceof PacketPlayInFlying && this.attack) {
			 long time = System.currentTimeMillis() - this.lastAttack;
			if (time >= 25L) {
				AlertData[] alertData = new AlertData[]{
						new AlertData("T", time),
						new AlertData("VL", vl)
				};

				if (++vl >= 10.0 && this.alert(player, AlertType.EXPERIMENTAL, alertData, false)) {
				}
			} else {
				vl -= 0.25;
			}
			this.attack = false;
		}
		this.setVl(vl);
	}

}
