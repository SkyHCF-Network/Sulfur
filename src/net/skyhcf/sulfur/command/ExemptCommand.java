package net.skyhcf.sulfur.command;

import net.skyhcf.sulfur.Sulfur;
import net.skyhcf.sulfur.player.PlayerData;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExemptCommand {

    @Command(names = {"sulfur exempt"}, permission = "anticheat.exempt")
    public static void exempt(CommandSender sender, @Param(name = "target")Player player){
        PlayerData playerData = Sulfur.instance.getPlayerDataManager().getPlayerData(player.getUniqueId());
        playerData.setExempt(!playerData.isExempt());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (playerData.isExempt() ? "&aYou have exempted &r" + player.getDisplayName() + "&r &afrom anticheat bans." : "&cYou have un-exempted &r" + player.getDisplayName() + "&r &cfrom anticheat bans.")));
    }

}
