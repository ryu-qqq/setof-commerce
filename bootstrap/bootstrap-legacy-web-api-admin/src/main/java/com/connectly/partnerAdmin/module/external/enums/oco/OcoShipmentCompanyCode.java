package com.connectly.partnerAdmin.module.external.enums.oco;


import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OcoShipmentCompanyCode {

    SHIP4(4,"GS Postbox 택배"),
    SHIP6(6,	"한진택배"),
    SHIP9(9,	"경동택배"),
    SHIP14(14,	"대신택배"),
    SHIP15(15,	"CJ대한통운"),
    SHIP20(20,	"로젠택배"),
    SHIP28(28,	"우체국택배"),
    SHIP32(32,	"일양로지스"),
    SHIP37(37,	"한진택배"),
    SHIP40(40,	"천일택배"),
    SHIP41(41,	"롯데택배"),
    SHIP42(42,	"한의사랑택배"),
    SHIP43(43,	"건영택배"),
    SHIP44(44,	"한덱스"),
    SHIP46(46,	"합동택배"),
    SHIP47(47,	"굿투럭"),
    SHIP48(48, "애니트랙"),
    SHIP49(49,	"SLX택배"),
    SHIP50(50,	"우리택배(구호남택배)"),
    SHIP51(51,	"CU 편의점택배"),
    SHIP52(52,	"수동처리(퀵, 방문수령 등)"),
    SHIP53(53,	"농협택배"),
    SHIP54(54,	"홈픽택배"),

    SHIP45(45,	"KGL네트웍스"),
    SHIP55(55, "하이택배");


    private final int code;
    private final String displayName;


    public static OcoShipmentCompanyCode of(ShipmentCompanyCode shipmentCompanyCode){
        return Arrays.stream(OcoShipmentCompanyCode.values())
                .filter(r -> Objects.equals(r.getDisplayName(), shipmentCompanyCode.getDisplayName()))
                .findAny()
                .orElseGet(()-> SHIP4);
    }


}
