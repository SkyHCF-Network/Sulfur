package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.CustomLocation;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class KillAuraC extends PacketCheck {

	private float lastYaw;

	public KillAuraC(PlayerData playerData) {
		super(playerData, "Kill Aura Type C");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (this.playerData.getLastTarget() == null) {
			return;
		}
		if (packet instanceof PacketPlayInFlying) {
			 PacketPlayInFlying flying = (PacketPlayInFlying) packet;
			if (flying.k() && !this.playerData.isAllowTeleport()) {
				 CustomLocation targetLocation = this.playerData.getLastPlayerPacket(this.playerData.getLastTarget(), MathUtil.pingFormula(this.playerData.getPing()));
				if (targetLocation == null) {
					return;
				}
				 CustomLocation playerLocation = this.playerData.getLastMovePacket();
				if (playerLocation.getX() == targetLocation.getX()) {
					return;
				}
				if (targetLocation.getZ() == playerLocation.getZ()) {
					return;
				}
				 float yaw = flying.g();
				if (yaw != this.lastYaw) {
					 float bodyYaw = MathUtil.getDistanceBetweenAngles(yaw, MathUtil.getRotationFromPosition(playerLocation, targetLocation)[0]);
					if (bodyYaw == 0.0f && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
						 int violations = this.playerData.getViolations(this, 60000L);
						if (!this.playerData.isBanning() && violations > 5) {
							this.ban(player);
						}
					}
				}
				this.lastYaw = yaw;
			}
		}
	}

}
