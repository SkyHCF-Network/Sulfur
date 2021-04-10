package net.skyhcf.sulfur.check.impl.aimassist;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.update.RotationUpdate;
import net.skyhcf.sulfur.check.checks.RotationCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import org.bukkit.entity.Player;

public class AimAssistE extends RotationCheck {

	private float lastYawRate;
	private float lastPitchRate;

	public AimAssistE(PlayerData playerData) {
		super(playerData, "Aim Type E");
	}

	@Override
	public void handleCheck(Player player, RotationUpdate update) {
		if (System.currentTimeMillis() > this.playerData.getLastAttackTime() + 10000L) {
			return;
		}

		float diffYaw = Math.abs(update.getFrom().getYaw() - update.getTo().getYaw());
		float diffPitch = Math.abs(update.getFrom().getPitch() - update.getTo().getPitch());

		float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
		float diffYawRate = Math.abs(this.lastYawRate - diffYaw);

		float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
		float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);

		if (diffPitch > 0.001 && diffPitch < 0.0094 && diffPitchRate > 1F && diffYawRate > 1F && diffYaw > 3F &&
				this.lastYawRate > 1.5 && (diffPitchRatePitch > 1F || diffYawRateYaw > 1F)) {

			AlertData[] alertData = new AlertData[]{
					new AlertData("DPR", diffPitchRate),
					new AlertData("DYR", diffYawRate),
					new AlertData("LPR", lastPitchRate),
					new AlertData("LYR", lastYawRate),
					new AlertData("DP", diffPitch),
					new AlertData("DY", diffYaw),
					new AlertData("DPRP", diffPitchRatePitch),
					new AlertData("DYRY", diffYawRateYaw)
			};

			this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
		}

		this.lastPitchRate = diffPitch;
		this.lastYawRate = diffYaw;
	}

}
