package net.skyhcf.sulfur.check.impl.aimassist;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.util.update.RotationUpdate;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.event.AlertType;
import org.bukkit.entity.Player;

public class AimAssistD extends RotationCheck {
	private float lastYawRate;
	private float lastPitchRate;

	public AimAssistD(PlayerData playerData) {
		super(playerData, "Aim Type D");
	}

	@Override
	public void handleCheck( Player player,  RotationUpdate update) {
		if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
			return;
		}

		float diffPitch = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
		float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());

		float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
		float diffYawRate = Math.abs(this.lastYawRate - diffYaw);

		float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
		float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);

		if (diffPitch < 0.009 && diffPitch > 0.001 && diffPitchRate > 1.0 && diffYawRate > 1.0 && diffYaw > 3.0 &&
		    this.lastYawRate > 1.5 && (diffPitchRatePitch > 1.0f || diffYawRateYaw > 1.0f)) {

			AlertData[] alertData = new AlertData[]{
					new AlertData("DPR", diffPitchRate),
					new AlertData("DYR", diffYawRate),
					new AlertData("LPR", this.lastPitchRate),
					new AlertData("LYR", this.lastYawRate),
					new AlertData("DP", diffPitch),
					new AlertData("DY", diffYaw),
			};

			this.alert(player, AlertType.EXPERIMENTAL, alertData, true);

			if (!this.playerData.isBanning() && this.playerData.getViolations(this, 1000L * 60 * 10) > 5) {
				this.ban(player);
			}
		}

		this.lastPitchRate = diffPitch;
		this.lastYawRate = diffYaw;
	}

}
