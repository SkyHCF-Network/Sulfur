package net.skyhcf.sulfur.command.menu;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.sulfur.command.menu.button.GlobalButton;
import net.skyhcf.sulfur.command.menu.button.ServerButton;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class LogsMainMenu extends PaginatedMenu {

    private final List<Document> logs;

    public LogsMainMenu(List<Document> logs){
        this.logs = logs;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Select a Server";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(0, new GlobalButton(logs));
        buttons.put(1, new ServerButton(SharedAPI.getServer("Practice"), logs));
        buttons.put(2, new ServerButton(SharedAPI.getServer("HCF"), logs));
        return buttons;
    }
}
