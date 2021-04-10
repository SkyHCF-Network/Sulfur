package net.skyhcf.sulfur.command;

import net.skyhcf.sulfur.Sulfur;
import net.skyhcf.sulfur.player.PlayerData;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand {

    @Command(names = { "sulfur ban" }, permission = "op")
    public static void execute( CommandSender player, @Param(name = "target")  Player target) {
         PlayerData playerData = Sulfur.instance.getPlayerDataManager().getPlayerData(target);
        playerData.setBanning(true);

         Bukkit.dispatchCommand(player, "ban " + player.getName() + " [Sulfur] Cheating");
    }

}