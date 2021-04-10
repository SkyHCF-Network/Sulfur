package net.skyhcf.sulfur.check.impl.killaura;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraT extends PacketCheck {

    private int lastTick;

    public KillAuraT(PlayerData playerData) {
        super(playerData, "Kill Aura Type T");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        double vl = this.getVl();
        if(packet instanceof PacketPlayInUseEntity){
            if(playerData.currentTick == lastTick){
                AlertData[] data = new AlertData[]{
                        new AlertData("Tick: ", playerData.currentTick)
                };
                if(vl++ > 0) alert(player, AlertType.RELEASE, data, true);
            }else vl = Math.max(0, vl-1);
        }else if(packet instanceof PacketPlayInBlockDig){
            lastTick = playerData.currentTick;
        }
        this.setVl(vl);
    }
}
