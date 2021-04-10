package net.skyhcf.sulfur;

;
import net.skyhcf.sulfur.alert.AlertsManager;
import net.skyhcf.sulfur.command.*;
import net.skyhcf.sulfur.handler.CustomMovementHandler;
import net.skyhcf.sulfur.handler.CustomPacketHandler;
import net.skyhcf.sulfur.listener.PlayerListener;
import net.skyhcf.sulfur.log.LogExportRunnable;
import net.skyhcf.sulfur.log.LogManager;
import net.skyhcf.sulfur.player.PlayerDataManager;
import net.frozenorb.qlib.command.FrozenCommandHandler;



import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


import net.hylist.HylistSpigot;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Sulfur extends JavaPlugin {

    public static Sulfur instance;

    private PlayerDataManager playerDataManager;
    private AlertsManager alertsManager;
    private LogManager logManager;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private double rangeVl = 100;
    private List<String> disabledChecks = new ArrayList<>();

    public static Sulfur instances;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;


        saveDefaultConfig();

        registerHandlers();
        registerManagers();
        registerListeners();
        registerCommands();
        registerExportLogsTimer();
        registerDatabase();
    }

    @Override
    public void onDisable() {
        getLogManager().exportAllLogs();
        mongoClient.close();
    }

    public boolean isAntiCheatEnabled() {
        return MinecraftServer.getServer().recentTps[0] > 19.0;
    }

    private void registerHandlers() {
        HylistSpigot.INSTANCE.addPacketHandler(new CustomPacketHandler(this));
        HylistSpigot.INSTANCE.addMovementHandler(new CustomMovementHandler(this));
    }

    private void registerManagers() {
        alertsManager = new AlertsManager();
        playerDataManager = new PlayerDataManager();
        logManager = new LogManager();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {
        FrozenCommandHandler.registerAll(this);
    }

    private void registerExportLogsTimer() {
        getServer().getScheduler().runTaskTimerAsynchronously(this, new LogExportRunnable(null), 600L, 600L);
    }

    private void registerDatabase() {
        if (getConfig().getBoolean("Mongo.Authentication.Enabled")) {
            ServerAddress serverAddress = new ServerAddress(
                    getConfig().getString("Mongo.Host"),
                    getConfig().getInt("Mongo.Port")
            );

            MongoCredential credential = MongoCredential.createCredential(
                    getConfig().getString("Mongo.Authentication.Username"),
                    "admin",
                    getConfig().getString("Mongo.Authentication.Password").toCharArray()
            );

            mongoClient = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(
                    getConfig().getString("Mongo.Host"),
                    getConfig().getInt("Mongo.Port")
            );
        }

        mongoDatabase = mongoClient.getDatabase(getConfig().getString("Mongo.DbName"));
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

}
