package net.skyhcf.sulfur.check.impl.speed;

import com.google.common.collect.Lists;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

import java.util.Arrays;
import java.util.UUID;

public class SpeedD extends PositionCheck {

    public SpeedD(PlayerData playerData) {
        super(playerData, "Speed Type D");
    }

    private int verbose;

    @Override
    public void handleCheck(Player player, PositionUpdate update) {

        double motionX = Math.abs(update.getTo().getX() - update.getFrom().getX());
        double motionZ = Math.abs(update.getTo().getZ() - update.getFrom().getZ());

        double speed = Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionZ, 2));

        if (player.getAllowFlight()
                || playerData.getDeathTicks() > 0
                || (System.currentTimeMillis() - this.playerData.getVelocityH() <= 0)
                || playerData.getIceTimer().hasNotPassed(20)
                || playerData.getBlockAboveTimer().hasNotPassed(15)) {
            verbose = 0;
            return;
        }

        if (player.getVehicle() != null
                || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 3)
                || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL
                || player.getNoDamageTicks() >= 3
                || player.hasMetadata("modmode")
                || player.hasMetadata("noflag")) {
            return;
        }

        double max = getBaseSpeed(player);

        if (speed >= max || motionX >= max || motionZ >= max) {
            if (verbose++ > 5) {
                AlertData[] alertData = new AlertData[]{
                        new AlertData("Speed", speed),
                        new AlertData("MotionX", motionX),
                        new AlertData("MotionZ", motionZ),
                };
                this.alert(player, AlertType.RELEASE, alertData, true);
                if(playerData == null) return;
                if(playerData.getViolations(this, 16_000L) >= 8 && !playerData.isBanning() && !playerData.isExempt()) {
                    playerData.setBanning(true);
                    StringBuilder alertDataBuilder = new StringBuilder();
                    int amount = alertData.length;
                    for (int var12 = 0; var12 < amount; var12++) {
                        AlertData s = alertData[var12];
                        alertDataBuilder.append(s.getName() + ": " + s.getValue());
                        alertDataBuilder.append(" ");
                    }
                    String dataFinal = ChatColor.stripColor(alertDataBuilder.toString().replace("&7", ""));
                    dataFinal = dataFinal.replace(" : ", ": ");
                    dataFinal = dataFinal.replace("MotionX", "MX").replace("MotionZ", "MZ");
                    dataFinal = dataFinal.replace("SPEED", "Speed").replace(" Check", "").replace(" ", " &7").replace("[", "");
                    AtmosphereShared.getInstance().getPunishmentManager().createPunishment(new Punishment(
                            UUID.randomUUID(),
                            player.getUniqueId(),
                            Profile.getConsoleUUID(),
                            null,
                            PunishmentType.BAN,
                            SharedAPI.getServer(Bukkit.getServerName()),
                            System.currentTimeMillis(),
                            Long.MAX_VALUE,
                            0L,
                            "[Sulfur] Cheating",
                            this.getName() + "; VL: " + this.getVl() + " " + dataFinal,
                            null,
                            Lists.newArrayList()
                    ), true);
                }
            }
        } else verbose = 0;
    }

    /**
     *
     * Checked base speed method to the method in ruby. (hopefully helps detect more cheaters)
     *
     */

    private float getBaseSpeed(Player player) {
        return 0.34f + (getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f) + ((player.getWalkSpeed() - 0.2f) * 1.6f);
    }

    private int getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }

}
