package net.skyhcf.sulfur.check.impl.range;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.util.CustomLocation;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class RangeA extends PacketCheck {

	private boolean sameTick;

	public RangeA(PlayerData playerData) {
		super(playerData, "Range Type A");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInUseEntity && !player.getGameMode().equals(GameMode.CREATIVE) && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L &&
				this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.sameTick) {
			final PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet;
			if (useEntity.c() == EnumEntityUseAction.ATTACK) {
				Entity targetEntity = useEntity.a(((CraftPlayer) player).getHandle().getWorld());

				if (targetEntity instanceof EntityPlayer) {
					Player target = (Player) targetEntity.getBukkitEntity();
					CustomLocation targetLocation = this.playerData.getLastPlayerPacket(target.getUniqueId(), MathUtil.pingFormula(this.playerData.getPing()));

					if (targetLocation == null) {
						return;
					}

					long diff = System.currentTimeMillis() - targetLocation.getTimestamp();
					long estimate = MathUtil.pingFormula(this.playerData.getPing()) * 50L;
					long diffEstimate = diff - estimate;

					if (diffEstimate >= 500L) {
						return;
					}

					CustomLocation playerLocation = this.playerData.getLastMovePacket();
					PlayerData targetData = getPlugin().getPlayerDataManager().getPlayerData(target);

					if (targetData == null) {
						return;
					}

					double range = Math.hypot(playerLocation.getX() - targetLocation.getX(), playerLocation.getZ() - targetLocation.getZ());

					if (range > 6.5) {
						return;
					}

					double threshold = 3.2;

					if (!targetData.isSprinting() || MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), targetLocation.getYaw()) <= 90.0) {
						threshold = 4.0;
					}

					double vl = this.getVl();

					if (range > threshold) {
						if (++vl >= 12.5) {
							final boolean ex = getPlugin().getRangeVl() == 0.0;

							AlertData[] alertData = new AlertData[]{
									new AlertData("P", range - threshold + 3.0),
									new AlertData("R", range),
									new AlertData("PI", this.playerData.getPing()),
									new AlertData("VL", vl),
							};

							if (this.alert(player, ex ? AlertType.EXPERIMENTAL : AlertType.RELEASE, alertData, !ex)) {
							} else {
								vl = 0.0;
							}
						}
					} else if (range >= 2.0) {
						vl -= 0.25;
					}

					this.setVl(vl);
					this.sameTick = true;
				}
			}
		} else if (packet instanceof PacketPlayInFlying) {
			this.sameTick = false;
		}
	}

}