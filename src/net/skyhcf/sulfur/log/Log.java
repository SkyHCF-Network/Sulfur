package net.skyhcf.sulfur.log;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

@AllArgsConstructor
@Getter
public class Log {

    private long timestamp;
    private UUID player;
    private String log;
    private double tps;

    public Document toDocument() {
        Document document = new Document();
        document.put("player", (Object) this.player.toString());
        document.put("server", (Object) Bukkit.getServer().getServerName());
        document.put("serverGroup", (Object) Bukkit.getServerGroup());
        document.put("log", (Object) this.log);
        document.put("time", (Object) this.timestamp);
        return document;
    }

    public Log(UUID player, String log, double tps) {
        this.timestamp = System.currentTimeMillis();
        this.player = player;
        this.log = log;
        this.tps = tps;
    }
}