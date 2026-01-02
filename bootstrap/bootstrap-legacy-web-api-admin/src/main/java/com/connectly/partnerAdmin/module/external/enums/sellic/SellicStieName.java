package com.connectly.partnerAdmin.module.external.enums.sellic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SellicStieName {
    LF_MALL("LF몰"),
    SSF_SHOP("SSF SHOP"),
    SI_VILLAGE("S.I.VILLAGE"),

    ;

    private final String siteName;

    public static SellicStieName of(String siteName){
        return Arrays.asList(SellicStieName.values()).stream()
                .filter(x -> x.getSiteName().equals(siteName))
                .findFirst()
                .orElse(SellicStieName.SI_VILLAGE);
    }


}
