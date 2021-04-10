package net.skyhcf.sulfur.check.impl.autoclicker;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedList;
import java.util.OptionalDouble;
import java.util.Queue;

public class AutoClickerM extends PacketCheck {

    private  Queue<Integer> flyingPackets;
    private int currentCount;

    public AutoClickerM(PlayerData playerData) {
        super(playerData, "Auto Clicker Type M");
        this.flyingPackets = new LinkedList<Integer>();
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            ++this.currentCount;
        } else if (packet instanceof PacketPlayInArmAnimation) {
            if (this.playerData.isDigging() || this.playerData.isPlacing()) {
                return;
            }
            if (this.currentCount >= 10) {
                this.currentCount = 0;
                return;
            }
            this.flyingPackets.add(this.currentCount);
            if (this.flyingPackets.size() >= 75) {
                this.handleFlyingPackets(player);
                this.flyingPackets.clear();
            }
            this.currentCount = 0;
        }
    }

    private void handleFlyingPackets(Player player) {
         double rangeDifference = this.getRangeDifference(this.flyingPackets);
        if (rangeDifference < 2.0) {
            AlertData[] alertData = new AlertData[]
                    {new AlertData("rangeDiff=%s", rangeDifference)};

            this.alert(player, AlertType.RELEASE, alertData, false);
        }
         double kurtosis = new Kurtosis().evaluate(this.flyingPackets.stream().mapToDouble(Number::doubleValue).toArray());
    }

    private double getRangeDifference (  Collection<? extends Number> numbers){
         OptionalDouble minOptional = numbers.stream().mapToDouble(Number::doubleValue).min();
         OptionalDouble maxOptional = numbers.stream().mapToDouble(Number::doubleValue).max();
        if (!minOptional.isPresent() || !maxOptional.isPresent()) {
            return 500.0;
        }
        return maxOptional.getAsDouble() - minOptional.getAsDouble();
    }
}

