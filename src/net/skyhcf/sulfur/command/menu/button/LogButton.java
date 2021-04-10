package net.skyhcf.sulfur.command.menu.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.utils.ColorUtil;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class LogButton extends Button {

    private final long time;
    private final String log;
    private final String server;

    public LogButton(long time, String log, String server){
        this.time = time;
        this.log = log;
        this.server = server;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + TimeUtil.formatDuration(System.currentTimeMillis() - time) + " ago";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&eServer&7: &c" + server);
        description.add("&eLog Information&7: &c" + log);
        description.add("&7&m" + BukkitChat.LINE);
        description = ColorUtil.formatList(description);
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.STAINED_CLAY;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 5;
    }
}
