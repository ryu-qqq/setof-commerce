package com.ryuqq.setof.storage.legacy.shipment.entity;

/**
 * LegacyShipmentCompanyCode - 레거시 배송 회사 코드 Enum.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum LegacyShipmentCompanyCode {
    CJ_LOGISTICS("CJ대한통운"),
    HANJIN("한진택배"),
    LOTTE("롯데택배"),
    LOGEN("로젠택배"),
    POST_OFFICE("우체국택배"),
    EPOST("우체국"),
    GS_POSTBOX("GS편의점"),
    DAESIN("대신택배"),
    ILYANG("일양로지스"),
    KDEXP("경동택배"),
    CHUNIL("천일택배"),
    CVSNET("편의점택배"),
    DIRECT("직접배송"),
    REFER_DETAIL("상세정보참조"),
    OTHER("기타");

    private final String displayName;

    LegacyShipmentCompanyCode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
