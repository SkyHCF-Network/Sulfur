package net.skyhcf.sulfur.check.impl.velocity;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import org.bukkit.entity.Player;

public class VelocityA extends PositionCheck {

	public VelocityA(PlayerData playerData) {
		super(playerData, "Velocity Type A");
	}

	@Override
	public void handleCheck( Player player,  PositionUpdate update) {
		int vl = (int) this.getVl();
		if (this.playerData.getVelocityY() > 0.0
				&& !this.playerData.isUnderBlock()
				&& !this.playerData.isWasUnderBlock()
				&& !this.playerData.isInLiquid()
				&& !this.playerData.isWasInLiquid()
				&& !this.playerData.isInWeb()
				&& !this.playerData.isWasInWeb()
				&& System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L
				&& System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L) {
			 int threshold = 10 + MathUtil.pingFormula(this.playerData.getPing()) * 2;
			if (++vl >= threshold) {
				AlertData[] alertData = new AlertData[]{
						new AlertData("VL", vl)
				};

				if (this.alert(player, AlertType.RELEASE, alertData, true)) {
					 int violations = this.playerData.getViolations(this, 60000L);
					if (!this.playerData.isBanning() && violations > Math.max(this.playerData.getPing() / 10L, 15L)) {
						this.ban(player);
					}
				}
				this.playerData.setVelocityY(0.0);
				vl = 0;
			}
		} else {
			vl = 0;
		}
		this.setVl(vl);
	}

}
