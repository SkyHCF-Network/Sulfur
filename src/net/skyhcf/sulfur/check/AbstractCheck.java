package net.skyhcf.sulfur.check;

import net.skyhcf.sulfur.Sulfur;
import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.player.PlayerData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import net.skyhcf.sulfur.event.AlertType;

import org.bukkit.entity.Player;

import net.skyhcf.sulfur.event.PlayerAlertEvent;
import net.skyhcf.sulfur.event.PlayerBanEvent;

@AllArgsConstructor
@Getter
public abstract class AbstractCheck<T> implements ICheck<T> {

    protected  PlayerData playerData;
    private  Class<T> clazz;
    private  String name;

    @Override
    public Class<? extends T> getType() {
        return this.clazz;
    }

    protected Sulfur getPlugin() {
        return Sulfur.instance;
    }

    protected Player getPlayer() {
        return Sulfur.instance.getServer().getPlayer(this.playerData.getUuid());
    }

    protected double getVl() {
        return this.playerData.getCheckVl(this);
    }

    protected void setVl( double vl) {
        this.playerData.setCheckVl(vl, this);
    }

    protected boolean alert(Player player, AlertType alertType, AlertData[] data, boolean violation) {
         String check = this.name + ((alertType != AlertType.RELEASE) ? (" (" + Character.toUpperCase(alertType.name().toLowerCase().charAt(0)) + alertType.name().toLowerCase().substring(1) + ")") : "");
         PlayerAlertEvent event = new PlayerAlertEvent(alertType, player, check, data);

        playerData.flaggedChecks.add(this);
        this.playerData.addViolation(this);
        getPlugin().getServer().getPluginManager().callEvent(event);

//        if (!event.isCancelled()) {
//            if (violation) {
//                playerData.flaggedChecks.add(this);
//                this.playerData.addViolation(this);
//            }
//            return true;
//        }

        return true;
    }

    protected boolean ban( Player player) {
        this.playerData.setBanning(true);

         PlayerBanEvent event = new PlayerBanEvent(player, this.name);

        this.getPlugin().getServer().getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    protected void randomBan(Player player, double rate) {
        this.playerData.setRandomBanRate(rate);
        this.playerData.setRandomBanReason(this.name);
        this.playerData.setRandomBan(true);

        getPlugin().getServer().getPluginManager().callEvent(new PlayerAlertEvent(AlertType.RELEASE, player, this.name));
    }

}
