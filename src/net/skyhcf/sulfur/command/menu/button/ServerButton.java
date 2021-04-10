package net.skyhcf.sulfur.command.menu.button;

import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.sulfur.command.menu.LogsMenu;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.print.Doc;
import java.util.List;

public class ServerButton extends Button {

    private final Server server;
    private final List<Document> logs;

    public ServerButton(Server server, List<Document> logs){
        this.server = server;
        this.logs = logs;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + server.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        switch(server.getId().toLowerCase()){
            case "practice": {
                return Material.POTION;
            } case "hcf": {
                return Material.BLAZE_POWDER;
            } case "souppvp": {
                return Material.MUSHROOM_SOUP;
            } case "kitmap": {
                return Material.BOW;
            } case "uhc": {
                return Material.GOLDEN_APPLE;
            } default: {
                return Material.STAINED_CLAY;
            }
        }
    }

    @Override
    public byte getDamageValue(Player player) {
        if(getMaterial(player) == Material.STAINED_CLAY){
            return 14;
        }else{
            return 0;
        }
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new LogsMenu(server.getName(), logs).openMenu(player);
    }
}
