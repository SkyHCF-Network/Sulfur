/*
package net.skyhcf.sulfur.command;


import net.skyhcf.sulfur.banwave.BanWave;
import net.skyhcf.sulfur.banwave.BanWaveManager;
import com.asterius.hydrogen.HydrogenAPI;
import com.asterius.hydrogen.profile.Profile;
import com.asterius.hydrogen.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import net.skyhcf.sulfur.Sulfur;

import java.util.UUID;

public class BanWaveCommand implements Listener {

    //This command runs the banwave when players are added to the banwave listeners
    @Command(names = {"tr banwave start", "banwave start"}, permission = "op")
    public static void execute(Player sender) {
        sender.sendMessage(ChatColor.GREEN + "Starting Ban wave...");

        Bukkit.broadcastMessage(CC.translate("&b&lTrident &7has activated a &bBan Wave&7."));
        Bukkit.broadcastMessage(CC.translate("&7Bans will happen shortly!"));
            BanWave.runBanWave();
        }

    private static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    //This command is to add players to the banwave whilst they're possibly cheating
    @Command(names = {"tr banwave add", "banwave add", "wave add"}, permission = "anticheat.banwave")
    public static void onAdd(Player sender, @Param(name = "player") Profile target) {
        BanWaveManager waveManager = Sulfur.instance.getBanWaveManager();
        if (waveManager.getPlayersToBan().contains(target.getUuid()))
            return;

        waveManager.addToBan(target.getUuid());
        sender.sendMessage(color(HydrogenAPI.getColoredName(target.getPlayer()) + " &fwas added to the banwave"));
    }

    //This command is to remove players that are not cheating / were falsely added to the judgement day
    @Command(names = {"tr banwave remove", "banwave remove", "wave remove"}, permission = "op")
    public static void onRemove(Player sender, @Param(name = "player") Profile target) {
        BanWaveManager waveManager = Sulfur.instance.getBanWaveManager();

        waveManager.removeFromBan(target.getUuid());
        sender.sendMessage(color(HydrogenAPI.getColoredName(target.getPlayer()) + " &fwas removed from the banwave"));
    }

    //This command opens a GUI, displaying the time they were added, the total amount of logs they've set off, and the PING/Sens of the player
    @Command(names = {"tr banwave list", "banwave list"},  permission = "anticheat.banwave")
    public static void banwaveList(Player sender) {
        BanWaveManager waveManager = Sulfur.instance.getBanWaveManager();

        StringBuilder stringBuilder = new StringBuilder();
        for(UUID uuid : waveManager.getPlayersToBan()){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null){
                //OFFLINE
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                stringBuilder.append(HydrogenAPI.getColoredName((Player) offlinePlayer) + ", ");
            }else{
                //ONLINE
                stringBuilder.append(HydrogenAPI.getColoredName(player) + ", ");
            }
        }

        sender.sendMessage("Banwave List: " + stringBuilder.toString());

        //CBA WITH FUCKING GUIS

//        Inventory inventory = Bukkit.createInventory(null, 54, "Trident | Banwave List");
//
//        waveManager.getPlayersToBan().stream().forEach(uuid -> {
//            OfflinePlayer user = Bukkit.getOfflinePlayer(uuid);
//            ItemStack item = new ItemStack(Material.PAPER);
//            ItemMeta meta = item.getItemMeta();
//            PlayerData data = AntiCheat.instance.getPlayerDataManager().getPlayerData(user.getUniqueId());
//            meta.setDisplayName(ZootAPI.getColoredName(user));
//            ArrayList<String> lores = new ArrayList<>();
//            lores.add("");
//            Profile profile = (Profile)Profile.getProfiles().get(user.getUniqueId());
//            Rank rank = profile == null ? Rank.getDefaultRank() : profile.getActiveRank();
//            lores.add(ChatColor.GRAY + "Rank: " + rank.getPrefix());
//            lores.add(ChatColor.GRAY + "Violation Level: " + ChatColor.RED + data.violations);
//            lores.add(ChatColor.GRAY + "Added At: " + ChatColor.RED + "" + TimeUtils.formatIntoMMSS((int) ((System.currentTimeMillis() - data.getAddedToBanwave()) / 1000L)));
//            lores.add(ChatColor.GRAY + "Ping: " + ChatColor.RED + data.getPing() + "ms");
//            lores.add(ChatColor.GRAY + "Mouse Sensitivity: " + ChatColor.RED + Math.round(data.getSensitivity() * 200) + "%");
//            lores.add("");
//            meta.setLore(lores);
//            item.setItemMeta(meta);
//            inventory.addItem(item);
//        });
//        sender.openInventory(inventory);
    }
}*/
