package net.skyhcf.sulfur.check.impl.motion;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.util.MathUtil;
import net.skyhcf.sulfur.util.Verbose;

public class MotionB extends PacketCheck {

    private int hits;
    private int value;
    private Verbose verbose = new Verbose();

    public MotionB(PlayerData playerData) {
        super(playerData, "Motion Type B");
    }

    @Override
    public void handleCheck(Player player, Packet type) {
        if(type instanceof PacketPlayInFlying){
            if(System.currentTimeMillis()-playerData.getLastAttack() < 150){
                double speed = playerData.getMovementSpeed();
                if(speed > 0){
                    if(hits > 0){
                      value++;
                    }
                    double max = value > 2 && playerData.isSprinting() ? getBaseSpeed(player, 0.281f) : getBaseSpeed(player, 0.282f);
                    if(speed > max && hits > 0){
                        if(verbose.flag(4, 600)){
                            AlertData[] alertData = new AlertData[]{
                                    new AlertData("Speed",  speed),
                                    new AlertData("Max", max),
                                    new AlertData("Hits", hits)
                            };

                            alert(player, AlertType.RELEASE, alertData, true);
                        }
                    }
                }else{
                    value = 0;
                }

            }else {
                value = 0;
            }

            hits = 0;
        }else if(type instanceof PacketPlayInUseEntity){
            hits++;
            playerData.setLastAttack(System.currentTimeMillis());
        }
    }

    private float getBaseSpeed(Player player, float normal) {
        return normal + (MathUtil.getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.059f) + ((player.getWalkSpeed() - 0.2f) * 1.6f);
    }

}
