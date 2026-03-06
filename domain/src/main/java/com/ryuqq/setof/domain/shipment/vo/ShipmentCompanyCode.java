package com.ryuqq.setof.domain.shipment.vo;

/** 택배사 코드. */
public enum ShipmentCompanyCode {
    CJ_LOGISTICS("CJ대한통운", "04"),
    HANJIN("한진택배", "05"),
    LOTTE("롯데택배", "08"),
    LOGEN("로젠택배", "06"),
    POST_OFFICE("우체국", "01"),
    ETC("기타", "99");

    private final String displayName;
    private final String code;

    ShipmentCompanyCode(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }
}
