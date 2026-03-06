package com.ryuqq.setof.domain.shipment.vo;

/** 배송 추적 정보 Value Object. */
public record TrackingInfo(String invoiceNo, ShipmentCompanyCode companyCode) {

    public TrackingInfo {
        if (invoiceNo == null || invoiceNo.isBlank()) {
            throw new IllegalArgumentException("송장번호는 필수입니다");
        }
        if (companyCode == null) {
            throw new IllegalArgumentException("택배사 코드는 필수입니다");
        }
    }

    public static TrackingInfo of(String invoiceNo, ShipmentCompanyCode companyCode) {
        return new TrackingInfo(invoiceNo, companyCode);
    }
}
