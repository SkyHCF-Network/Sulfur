package net.skyhcf.sulfur.command.menu.button;

import net.frozenorb.qlib.menu.Button;
import net.skyhcf.sulfur.command.menu.LogsMenu;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GlobalButton extends Button {

    private final List<Document> logs;

    public GlobalButton(List<Document> logs){
        this.logs = logs;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + "Global";
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.STAINED_CLAY;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 14;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new LogsMenu("global", logs).openMenu(player);
    }
}
