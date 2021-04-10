package net.skyhcf.sulfur.command;

import net.frozenorb.qlib.command.Command;
import net.skyhcf.sulfur.Sulfur;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StaffAlertsCommand {


    @Command(names = { "alerts", "sulfur alerts" }, permission = "srmod.alerts")
    public static void execute(Player sender) {
        Sulfur.instance.getAlertsManager().toggleAlerts(sender);
        Sulfur.instance.getPlayerDataManager().getPlayerData(sender).staffalerts = !Sulfur.instance.getPlayerDataManager().getPlayerData(sender).staffalerts;
        sender.sendMessage(Sulfur.instance.getAlertsManager().hasAlertsToggled(sender) ? (ChatColor.GREEN + "Sucessfully enabled Alerts.") : (ChatColor.RED + "Succesfully disabled Alerts."));
    }
}
