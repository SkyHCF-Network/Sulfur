package net.skyhcf.sulfur.check.impl.speed;


import net.skyhcf.sulfur.check.checks.PositionCheck;
import net.skyhcf.sulfur.util.update.PositionUpdate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;

public class SpeedA extends PositionCheck {

    private int verbose;
    private double average;

    public SpeedA(PlayerData playerData) {
        super(playerData, "Speed Type A");
    }

    @Override
    public void handleCheck(Player player, PositionUpdate update) {
        double motionX = Math.abs(update.getTo().getX() - update.getFrom().getX());
        double motionZ = Math.abs(update.getTo().getZ() - update.getFrom().getZ());

        double speed = Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionZ, 2));

        if(player.getAllowFlight()
                || playerData.getDeathTicks() > 0
                || System.currentTimeMillis() - playerData.getLastVelocity() < 650) {
            verbose = 0;
            return;
        }

        if (player.getVehicle() != null
                || (player.getMaximumNoDamageTicks() < 20
                && player.getNoDamageTicks() >= 1)
                || this.playerData.getVelocityH() <= 0 || player.isFlying()
                || player.getGameMode() != GameMode.SURVIVAL
                || player.getNoDamageTicks() >= 1
                || player.hasMetadata("modmode")
                || player.hasMetadata("noflag")) {
            return;
        }

        double max = 0.345;
        max += 0.4 * getPotionEffectLevel(player, PotionEffectType.SPEED);

        average = ((average + 14) * speed) / 15;

        if(average > max){
            if(verbose++ > 4){
                AlertData[] alertData = new AlertData[]{
                        new AlertData("Speed", speed),
                        new AlertData("MotionX", motionX),
                        new AlertData("MotionZ", motionZ),
                };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        }else verbose -= verbose > 0 ? 1 : 0;

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
