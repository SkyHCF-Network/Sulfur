package net.skyhcf.sulfur.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumClientType implements ClientType {

    COSMIC_CLIENT(false, "CosmicClient"),
    CHEAT_BREAKER(false, "CheatBreaker"),
    Lunar_Client(false, "Lunar-Client"),
    VANILLA(false, "Vanilla"),
    FORGE(false, "Forge-Client"),
    OCMC(false, "OCMC-Client", "OCMC");

    
    private boolean hacked;
    private String name;
    private String payloadTag;

    EnumClientType(boolean hacked, String name) {
        this.hacked = hacked;
        this.name = name;
    }

}
