package com.connectly.partnerAdmin.module.external.enums.sellic;

import java.util.Arrays;
import java.util.Objects;

import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SellicShipmentCompanyCode {

    SHIP1000(1000,"CJ대한통운"),
    SHIP1001(1001,	"한진택배"),
    SHIP1002(1002,	"우체국택배"),
    SHIP1003(1003,	"롯데택배"),
    SHIP1004(1004,	"로젠택배"),
    SHIP1005(1005,	"동부익스프레스"),
    SHIP1006(1006,	"KGB"),
    SHIP1007(1007,	"GTX로지스"),
    SHIP1008(1008,	"일양로지스"),
    SHIP1009(1009,	"천일택배"),
    SHIP1010(1010,	"대신택배"),
    SHIP1011(1011,	"경동택배"),
    SHIP1012(1012,	"합동택배"),
    SHIP1023(1023,	"지정없음 (구매자자율)"),
    SHIP1024(1024,	"수동처리(퀵, 방문수령 등)"),
    SHIP1026(1026,	"삼성전자물류"),
    SHIP1027(1027,	"위니아딤채"),
    SHIP0000(0, "NONE");


    private final int code;
    private final String displayName;

    public static SellicShipmentCompanyCode of(ShipmentCompanyCode shipmentCompanyCode){
        if(shipmentCompanyCode.equals(ShipmentCompanyCode.SHIP146)){
            return SellicShipmentCompanyCode.SHIP1003;
        }

        return Arrays.stream(SellicShipmentCompanyCode.values())
                .filter(r -> Objects.equals(r.getDisplayName(), shipmentCompanyCode.getDisplayName()))
                .findAny()
                .orElseGet(()-> SHIP1000);
    }

    public boolean isNoMapping(){
        return this.equals(SHIP0000);
    }


}
