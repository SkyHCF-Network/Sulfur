package net.skyhcf.sulfur.command;

import net.skyhcf.sulfur.Sulfur;
import net.skyhcf.sulfur.command.menu.LogsMainMenu;
import net.skyhcf.sulfur.log.Log;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.PaginatedOutput;
import net.frozenorb.qlib.util.TimeUtils;
import net.frozenorb.qlib.uuid.FrozenUUIDCache;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogsCommand {

    @Command(names = {"logs", "records"}, permission = "anticheat.logs", async = true)
    public static void execute(CommandSender sender, @Param(name = "target")  UUID target, @Param(name = "page", defaultValue = "1")  int page) {
        Iterable<Document> mongoDocs = Sulfur.instance.getMongoDatabase().getCollection("logs").find(new Document("player", target.toString()));
        Iterable<Log> sessionLogs = Sulfur.instance.getLogManager().getLogQueue();
        List<Document> logs = new ArrayList<>();
        Set<Long> caught = new HashSet<>();

        for ( Document mongoDocument : mongoDocs) {
            long time = mongoDocument.getLong("time");
            logs.add(mongoDocument);
            caught.add(time);
        }

        for (Log log : sessionLogs) {
            if (log.getPlayer().equals(target) && caught.add(log.getTimestamp())) {
                logs.add(log.toDocument());
            }
        }

        //sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&m-------------------------------------------------"));

        if (logs.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No records for " + FrozenUUIDCache.name(target) + " found.");
            //sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&m-------------------------------------------------"));
            return;
        }

        logs = Lists.reverse(logs);

        new LogsMainMenu(logs).openMenu((Player) sender);



/*        List<Document> finalLogs = logs;
        new PaginatedOutput<Document>() {
            public String getHeader( int page,  int maxPages) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &cTotal logs: &e" + finalLogs.size() + ""));
                return ChatColor.translateAlternateColorCodes('&',  "&e" + FrozenUUIDCache.name(target) + "&c's Logs (&e" + page + "&7/&e" + maxPages + "&c)");
            }

            public String format( Document entry,  int index) {
                String message = entry.getString("log").replaceAll("failed ", "");
                return ChatColor.YELLOW + " - [" + TimeUtils.formatIntoMMSS((int) ((System.currentTimeMillis() - entry.getLong("time")) / 1000L)) + " ago on " + entry.getString("server") + "] " +  ((sender.hasPermission("anticheat.logs.view") ? ChatColor.YELLOW + message : ChatColor.RED + "You do not have permission to view this log."));
            }
        }.display(sender, page, logs);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&m-------------------------------------------------"));*/
    }

}