package net.skyhcf.sulfur.check.impl.scaffold;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.CustomLocation;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class ScaffoldB extends PacketCheck {

	private long lastPlace;
	private boolean place;

	public ScaffoldB(PlayerData playerData) {
		super(playerData, "Scaffold Type B");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		double vl = this.getVl();
		if (packet instanceof PacketPlayInBlockPlace && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
			 CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
			if (lastMovePacket == null) {
				return;
			}
			 long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
			if (delay <= 25.0) {
				this.lastPlace = System.currentTimeMillis();
				this.place = true;
			} else {
				vl -= 0.25;
			}
		} else if (packet instanceof PacketPlayInFlying && this.place) {
			 long time = System.currentTimeMillis() - this.lastPlace;
			if (time >= 25L) {
				if (++vl >= 10.0) {
					AlertData[] alertData = new AlertData[]{
							new AlertData("T", time),
							new AlertData("VL", vl)
					};

					this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
				}
			} else {
				vl -= 0.25;
			}
			this.place = false;
		}
		this.setVl(vl);
	}

}
