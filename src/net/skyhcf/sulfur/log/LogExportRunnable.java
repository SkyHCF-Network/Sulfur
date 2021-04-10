package net.skyhcf.sulfur.log;

import net.skyhcf.sulfur.Sulfur;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class LogExportRunnable implements Runnable {

    private  CommandSender sender;
    private  Queue<Log> logs;
    
    public LogExportRunnable( CommandSender sender) {
        this(sender, Sulfur.instance.getLogManager().getLogQueue());
    }
    
    @Override
    public void run() {
        if (this.logs.isEmpty()) {
            if (this.sender != null) {
                this.sender.sendMessage(ChatColor.RED + "There are no logs to be exported.");
            }

            return;
        }

         long start = System.currentTimeMillis();
         List<Document> logsDocuments = new ArrayList<>();
         Iterator<Log> logIterator = this.logs.iterator();

        while (logIterator.hasNext()) {
            Log current = logIterator.next();
            logsDocuments.add(current.toDocument());
            logIterator.remove();
        }

        Sulfur.instance.getMongoDatabase().getCollection("logs").insertMany(logsDocuments);

         long timeTaken = System.currentTimeMillis() - start;

        if (this.sender != null) {
            this.sender.sendMessage(ChatColor.GREEN + "Exported " + logsDocuments.size() + " logs in " + timeTaken + "ms.");
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Exported " + logsDocuments.size() + " logs in " + timeTaken + "ms.");
        }
    }
}