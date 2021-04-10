package net.skyhcf.sulfur.alert;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;

public class AlertsManager {

    @Getter
    private  Set<UUID> alertsToggled = new HashSet<>();
    
    public boolean hasAlertsToggled( Player player) {
        return this.alertsToggled.contains(player.getUniqueId());
    }
    
    public void toggleAlerts( Player player) {
        if (!this.alertsToggled.remove(player.getUniqueId())) {
            this.alertsToggled.add(player.getUniqueId());
        }
    }

}