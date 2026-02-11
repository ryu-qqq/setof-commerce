package com.ryuqq.setof.application.legacy.user.dto.response;

/**
 * 레거시 배송지 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param shippingAddressId 배송지 ID
 * @param receiverName 수취인명
 * @param shippingAddressName 배송지명
 * @param addressLine1 주소1
 * @param addressLine2 주소2
 * @param zipCode 우편번호
 * @param country 국가
 * @param deliveryRequest 배송 요청사항
 * @param phoneNumber 전화번호
 * @param defaultYn 기본 배송지 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyShippingAddressResult(
        long shippingAddressId,
        String receiverName,
        String shippingAddressName,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber,
        String defaultYn) {

    public static LegacyShippingAddressResult of(
            long shippingAddressId,
            String receiverName,
            String shippingAddressName,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber,
            String defaultYn) {
        return new LegacyShippingAddressResult(
                shippingAddressId,
                receiverName,
                shippingAddressName,
                addressLine1,
                addressLine2,
                zipCode,
                country,
                deliveryRequest,
                phoneNumber,
                defaultYn);
    }

    public boolean isDefault() {
        return "Y".equals(defaultYn);
    }
}
