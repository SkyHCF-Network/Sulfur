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

public class SpeedC extends PositionCheck {


    public SpeedC(PlayerData playerData) {
        super(playerData, "Speed Type C");
    }

    private boolean lastGround;
    private double speed;

    @Override
    public void handleCheck(Player player, PositionUpdate update) {
        if (player.getAllowFlight()
                || !player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        double x = Math.abs(Math.abs(update.getTo().getX()) - Math.abs(update.getFrom().getX()));
        double z = Math.abs(Math.abs(update.getTo().getZ()) - Math.abs(update.getFrom().getZ()));
        speed = Math.sqrt(x * x + z * z);


        double max = 0.64f;

        for (PotionEffect effect : this.getPlayer().getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                max += effect.getAmplifier() + 1;
            }
        }

        boolean ground = playerData.isOnGround();

        if (ground && !lastGround && this.speed > max) {
            if (player.hasMetadata("modmode")) return;
            if (player.hasMetadata("noflag")) return;
            AlertData[] alertData = new AlertData[]{
                    new AlertData("Speed", speed),
            };
            this.alert(player, AlertType.RELEASE, alertData, false);
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
                        this.getName() + "; VL: " + this.getVl() + dataFinal,
                        null,
                        Lists.newArrayList()
            ), true);
            }
        }

        this.lastGround = ground;

    }
}

