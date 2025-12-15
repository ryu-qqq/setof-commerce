package com.setof.connectly.module.event.enums;

import java.util.Arrays;

public enum EventProductType {
    NONE,
    SALE,
    RAFFLE,
    ;

    public boolean isRaffle() {
        return this.equals(RAFFLE);
    }

    public static EventProductType of(String eventProductType) {
        return Arrays.stream(EventProductType.values())
                .filter(r -> r.name().equals(eventProductType))
                .findAny()
                .orElseGet(() -> EventProductType.NONE);
    }
}
