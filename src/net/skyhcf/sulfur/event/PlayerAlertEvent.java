package net.skyhcf.sulfur.event;

import net.skyhcf.sulfur.alert.AlertData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerAlertEvent extends Event implements Cancellable {

	private static  HandlerList HANDLER_LIST;

	static {
		HANDLER_LIST = new HandlerList();
	}

	private  AlertType alertType;
	private  Player player;
	private  String checkName;
	private  AlertData[] data;
	@Setter private boolean cancelled;

	public PlayerAlertEvent(AlertType alertType, Player player, String checkName) {
		this(alertType, player, checkName, new AlertData[0]);
	}

	public PlayerAlertEvent(AlertType alertType, Player player, String checkName, AlertData[] data) {
		this.alertType = alertType;
		this.player = player;
		this.checkName = checkName;
		this.data = data;
	}

	public String concatData() {
		if (this.data.length == 0) {
			return "";
		} else {
			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < this.data.length; i++) {
				AlertData alertData = this.data[i];

				builder.append(alertData.getName());
				builder.append(" ");
				builder.append(alertData.getValue().toString());

				if (i != this.data.length - 1) {
					builder.append(", ");
				}
			}

			return builder.toString();
		}
	}

	public HandlerList getHandlers() {
		return PlayerAlertEvent.HANDLER_LIST;
	}

	public static HandlerList getHandlerList() {
		return PlayerAlertEvent.HANDLER_LIST;
	}

}
