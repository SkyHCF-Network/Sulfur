package net.skyhcf.sulfur.listener;


import net.skyhcf.sulfur.alert.AlertData;
import net.skyhcf.sulfur.event.PlayerAlertEvent;
import net.skyhcf.sulfur.event.PlayerBanEvent;
import net.skyhcf.sulfur.log.Log;
import net.skyhcf.sulfur.player.PlayerData;
import net.skyhcf.sulfur.Sulfur;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	private Map<UUID, Long> lastFire = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Sulfur.instance.getPlayerDataManager().addPlayerData(event.getPlayer());



		Sulfur.instance.getServer().getScheduler().runTaskLaterAsynchronously(Sulfur.instance, () -> {
			 PlayerConnection playerConnection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;

			playerConnection.sendPacket(new PacketPlayOutCustomPayload(
					"REGISTER",
					new PacketDataSerializer(Unpooled.wrappedBuffer("CB-Client".getBytes()))
			));

			playerConnection.sendPacket(new PacketPlayOutCustomPayload(
					"REGISTER",
					new PacketDataSerializer(Unpooled.wrappedBuffer("Lunar-Client".getBytes()))
			));

			playerConnection.sendPacket(new PacketPlayOutCustomPayload(
					"REGISTER",
					new PacketDataSerializer(Unpooled.wrappedBuffer("FML|HS".getBytes()))
			));

			playerConnection.sendPacket(new PacketPlayOutCustomPayload(
					"REGISTER",
					new PacketDataSerializer(Unpooled.wrappedBuffer("CC".getBytes()))
			));
		}, 10L);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Sulfur.instance.getAlertsManager().hasAlertsToggled(event.getPlayer())) {
			Sulfur.instance.getAlertsManager().toggleAlerts(event.getPlayer());
		}

		Sulfur.instance.getPlayerDataManager().removePlayerData(event.getPlayer());
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		 Player player = event.getPlayer();
		 PlayerData playerData = Sulfur.instance.getPlayerDataManager().getPlayerData(player);

		if (playerData != null) {
			playerData.setInventoryOpen(false);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onProjectileLaunch(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Player shooter = (Player) event.getEntity();

		Long lastFired = this.lastFire.get(shooter.getUniqueId());

		if (lastFired != null && System.currentTimeMillis() - lastFired < 500L) {
			event.setCancelled(true);
			this.lastFire.put(shooter.getUniqueId(), System.currentTimeMillis());
			return;
		}

		this.lastFire.put(shooter.getUniqueId(), System.currentTimeMillis());
	}

	@EventHandler
	public void onPlayerAlert(PlayerAlertEvent event) {
		if (!Sulfur.instance.isAntiCheatEnabled()) {
			event.setCancelled(true);
		} else {
			Player player = event.getPlayer();
			if (player != null) {
				PlayerData playerData = Sulfur.instance.getPlayerDataManager().getPlayerData(player);
				if (playerData != null) {
					FancyMessage basicAlerts = new FancyMessage();
					basicAlerts.text(ChatColor.translateAlternateColorCodes('&', "&7[&e&r" + player.getDisplayName() + "&r &7flagged &c" + event.getCheckName() + "&7.")).tooltip(ChatColor.translateAlternateColorCodes('&', "Click to teleport to " + player.getDisplayName() + "&r.")).command("/tp " + player.getName());
					FancyMessage devAlerts = new FancyMessage();
					StringBuilder alertData = new StringBuilder();
					AlertData[] var10 = event.getData();
					int var11 = var10.length;
					for (int var12 = 0; var12 < var11; var12++) {
						AlertData s = var10[var12];
						alertData.append(s.getName() + ": " + s.getValue());
						alertData.append(" ");
					}
					String dataFinal = alertData.toString();
					dataFinal = dataFinal.replace(" : ", ": ");
					String checkName = event.getCheckName().replace("Check ", "").replace(" Check", "");
					dataFinal = dataFinal.replace("MotionX", "MX").replace("MotionZ", "MZ");
					dataFinal = dataFinal.replace("SPEED", "Speed").replace(" Check", "").replace(" ", " &7").replace("[", "");
					devAlerts.text(ChatColor.translateAlternateColorCodes('&', "&7[&e⚠&7]&r " + player.getDisplayName() + "&r &7flagged &c" + checkName + "&7. " + dataFinal)).tooltip(ChatColor.translateAlternateColorCodes('&', "Click to teleport to " + player.getDisplayName() + "&r.")).command("/tp " + player.getName());
					if (System.currentTimeMillis() - playerData.getLastFlag() > 75L)
						Sulfur.instance.getAlertsManager().getAlertsToggled().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> {
							PlayerData data = Sulfur.instance.getPlayerDataManager().getPlayerData(p);
							if (data.devalerts && p.hasPermission("anticheat.alerts")) {
								devAlerts.send(p);
							} else if (data.staffalerts && p.hasPermission("srmod.alerts")) {
								basicAlerts.send(p);
							}
						});
					Sulfur.instance.getLogManager().getLogQueue().add(new Log(event.getPlayer().getUniqueId(), event.getCheckName() + ", " + event.concatData(), Bukkit.spigot().getTPS()[0]));
					playerData.setLastFlag(System.currentTimeMillis());
				}
			}
		}
	}

	public static String formatDecimal(double decimal){
		return new DecimalFormat("#.##").format(decimal);
	}

	@EventHandler
	public void onPlayerBan(PlayerBanEvent event) {
		if (!Sulfur.instance.isAntiCheatEnabled()) {
			event.setCancelled(true);
			return;
		}

		Player player = event.getPlayer();

		if (player == null) {
			return;
		}

/*		Sulfur.instance.getServer().getScheduler().runTask(Sulfur.instance, () -> {
			Sulfur.instance.getServer().dispatchCommand(Sulfur.instance.getServer().getConsoleSender(), "ban " + player.getDisplayName() + " [Anticheat] Unfair Advantage");

			PlayerAlertEvent alertEvent = new PlayerAlertEvent(AlertType.RELEASE, player, "Auto-banned for " + event.getReason());
			Sulfur.instance.getServer().getPluginManager().callEvent(alertEvent);
		});*/
	}

	public String color( String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
