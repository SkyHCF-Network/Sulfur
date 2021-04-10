package net.skyhcf.sulfur.check.impl.badpackets;

import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.check.checks.PacketCheck;
import net.skyhcf.sulfur.event.AlertType;
import net.skyhcf.sulfur.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInTransaction;
import org.bukkit.entity.Player;

//Detects most freecams/blinks almost instantly
public class BadPacketsN extends PacketCheck {

    public BadPacketsN(PlayerData playerData) {
        super(playerData, "Invalid Packets Type N");
    }

    private long lastFlying;
    private int vl;

    @Override
    public void handleCheck(Player player, Packet type) {
        if(type instanceof PacketPlayInTransaction) {
            if(Math.abs(System.currentTimeMillis() - lastFlying) > 250L) {
                this.alert(player, AlertType.RELEASE, new AlertData[] { new AlertData("LF ", lastFlying) }, true);

                //TODO: Make sure it doesnt false (it shouldnt)
                //if(++vl > 10) this.ban(player);
            }
        } else if(type instanceof PacketPlayInFlying) {
            lastFlying = System.currentTimeMillis(); //Could use ticks, but it really doesnt matter
        }
    }

}
