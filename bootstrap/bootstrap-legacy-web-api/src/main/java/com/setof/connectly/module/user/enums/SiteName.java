package com.setof.connectly.module.user.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SiteName {
    OUR_MALL("SET_OF 몰", 0),
    MUSTIT("MUSTIT", 1),
    SEWON("SEWON", 2),

    SSF_SHOP("SSF_SHOP 몰", 1076),
    OCO("OCO 몰", 1083);

    private final String displayName;
    private final long siteId;

    public static SiteName of(long siteId) {
        return Arrays.stream(SiteName.values())
                .filter(r -> r.getSiteId() == siteId)
                .findAny()
                .orElseGet(() -> SiteName.OUR_MALL);
    }
}
