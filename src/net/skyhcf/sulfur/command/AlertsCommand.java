package net.skyhcf.sulfur.command;

import net.skyhcf.sulfur.Sulfur;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AlertsCommand {

	@Command(names = { "sulfur dev", "devalerts"}, permission = "anticheat.alerts")
	public static void execute(Player sender) {
		Sulfur.instance.getAlertsManager().toggleAlerts(sender);
		Sulfur.instance.getPlayerDataManager().getPlayerData(sender).devalerts = !Sulfur.instance.getPlayerDataManager().getPlayerData(sender).devalerts;
		sender.sendMessage(Sulfur.instance.getAlertsManager().hasAlertsToggled(sender) ? (ChatColor.GREEN + "Sucessfully enabled Dev-Alerts.") : (ChatColor.RED + "Succesfully disabled Dev-Alerts."));
	}

}