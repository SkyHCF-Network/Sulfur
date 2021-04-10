package net.skyhcf.sulfur.util;

import lombok.Getter;
import net.skyhcf.sulfur.player.PlayerData;

/**
 * Created on 22/07/2020 Package me.jumba.sparky.util.time
 */
@Getter
public class EventTimer {

    private int tick;
    private int max;

    private PlayerData user;

    public EventTimer(int max, PlayerData user) {
        this.tick = 0;
        this.max = max;
        this.user = user;
    }

    public boolean hasNotPassed(int ctick) {
        return (this.user.currentTick > ctick && (this.user.currentTick - tick) < ctick);
    }

    public boolean passed(int ctick) {
        return (this.user.currentTick > ctick && (this.user.currentTick - tick) > ctick);
    }

    public boolean hasNotPassed() {
        return (this.user.currentTick > this.max && (this.user.currentTick - tick) < this.max);
    }

    public boolean passed() {
        return (this.user.currentTick > this.max && (this.user.currentTick - tick) > this.max);
    }

    public void reset() {
        this.tick = this.user.currentTick;
    }
}
