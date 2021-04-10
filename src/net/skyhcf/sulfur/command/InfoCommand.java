package net.skyhcf.sulfur.command;

import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.sulfur.player.PlayerData;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;

import net.skyhcf.sulfur.Sulfur;
import org.apache.commons.lang.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public class InfoCommand {

	@Command(names = { "sulfur info" }, permission = "anticheat.info")
	public static void execute( CommandSender sender, @Param(name = "target")  Player target) {
		PlayerData targetData = Sulfur.instance.getPlayerDataManager().getPlayerData(target);
		double[] tps = Bukkit.spigot().getTPS();
		String[] tpsAvg = new String[tps.length];

		for (int i = 0; i < tps.length; i++) {
			tpsAvg[i] = formatAdvancedTps(tps[i]);
		}

		Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(target.getUniqueId());
		Server server = AtmosphereShared.getInstance().getServerManager().getServer(Bukkit.getServerName());
		Rank targetRank = profile.getHighestGrantOnScope(server).getRank();
		sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "------------------------------");
		sender.sendMessage(ChatColor.YELLOW + "Sulfur lookup from all time:");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + "Player Information:");
		sender.sendMessage(ChatColor.GRAY + " Client Version: " + ChatColor.RED + getVersion((CraftPlayer) target)); //Gets the players client using Client ID
		sender.sendMessage(ChatColor.GRAY + " Client Brand: " + ChatColor.RED + targetData.getClient().getName()); //Gets the players client brand by sending a REGISTER Packet on-join to verify the players client
		sender.sendMessage(ChatColor.GRAY + " Rank: " + targetRank.getColor() + targetRank.getDisplayName()); //Gets the players rank via Atmosphere-API
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + "Statistics:");
		sender.sendMessage(ChatColor.GRAY + " Total Logs: " + ChatColor.RED +  targetData.violations); //Gets the players current logs they've flagged within the time they were on
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Average CPS: " + ChatColor.RED + targetData.getLastCps()); //Gets the players LAST-CPS so whenever a player clicks it registers there clicks in AutoClicker-A
		sender.sendMessage(ChatColor.GRAY + "Average Ping: " + ChatColor.RED + targetData.getPing() + " ms"); //Gets the players latest PING to the server
		sender.sendMessage(ChatColor.GRAY + "Average TPS: " + ChatColor.GREEN + StringUtils.join(tpsAvg, ", ")); //Gets the servers TPS last from 1m. 5m, 15m
		sender.sendMessage(ChatColor.GRAY + "Mouse Sensitivity: " + ChatColor.RED + Math.round(targetData.getSensitivity() * 200) + "%"); //Gets the players mouse-sensitivity buy calculating the players sens mappings
		sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "------------------------------");
	}

	private static String formatAdvancedTps(double tps) {
		return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.min(Math.round(tps * 100.0D) / 100.0, 20.0);
	}

	private static String getVersion(CraftPlayer player) {
		int i = player.getHandle().playerConnection.networkManager.getVersion();
		return i == 5 ? "1.7.10" : (i == 47 ? "1.8" : "N/A");
	}

}