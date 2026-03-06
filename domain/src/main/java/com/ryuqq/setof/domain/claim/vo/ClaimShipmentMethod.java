package com.ryuqq.setof.domain.claim.vo;

/**
 * 클레임 수거 방법 Value Object.
 *
 * <p>수거 방법 유형, 택배사 코드, 운송장 번호를 포함합니다.
 *
 * @param type 수거 방법 유형
 * @param carrierCode 택배사 코드 (nullable)
 * @param trackingNumber 운송장 번호 (nullable)
 */
public record ClaimShipmentMethod(
        ShipmentMethodType type, String carrierCode, String trackingNumber) {

    public ClaimShipmentMethod {
        if (type == null) {
            throw new IllegalArgumentException("수거 방법은 필수입니다");
        }
    }

    public static ClaimShipmentMethod of(
            ShipmentMethodType type, String carrierCode, String trackingNumber) {
        return new ClaimShipmentMethod(type, carrierCode, trackingNumber);
    }

    /**
     * 판매자 수거 방법으로 생성합니다. 택배사 코드와 운송장 번호는 이후 설정합니다.
     *
     * @return 판매자 수거 방법
     */
    public static ClaimShipmentMethod sellerArranged() {
        return new ClaimShipmentMethod(ShipmentMethodType.SELLER_ARRANGED, null, null);
    }

    /**
     * 운송장 정보가 존재하는지 확인합니다.
     *
     * @return 운송장 정보 존재 여부
     */
    public boolean hasTrackingInfo() {
        return trackingNumber != null && !trackingNumber.isBlank();
    }
}
