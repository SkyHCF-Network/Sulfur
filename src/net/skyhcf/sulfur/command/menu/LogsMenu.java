package net.skyhcf.sulfur.command.menu;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.utils.button.BackButton;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.sulfur.command.menu.button.LogButton;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class LogsMenu extends PaginatedMenu {

    private final String server;
    private final List<Document> logs;

    public LogsMenu(String server, List<Document> logs){
        this.server = server;
        this.logs = logs;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, new BackButton(new LogsMainMenu(logs)));
        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Logs " + "(" + (server == "global" ? "Global" : server) + ")";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int index = 0;
        if(!server.equalsIgnoreCase("global")) {
            for (Document doc : logs) {
                if (doc.getString("server").equalsIgnoreCase(server)) {
                    buttons.put(index++, new LogButton(doc.getLong("time"), doc.getString("log"), doc.getString("server")));
                }
            }
        }else{
            for (Document doc : logs) {
                buttons.put(index++, new LogButton(doc.getLong("time"), doc.getString("log"), doc.getString("server")));
            }
        }
        return buttons;

    }
}
