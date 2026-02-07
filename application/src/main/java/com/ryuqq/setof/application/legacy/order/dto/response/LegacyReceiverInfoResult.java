package com.ryuqq.setof.application.legacy.order.dto.response;

/**
 * LegacyReceiverInfoResult - 레거시 수령자 정보 결과 DTO.
 *
 * @param receiverName 수령자명
 * @param receiverPhoneNumber 수령자 전화번호
 * @param addressLine1 주소 1
 * @param addressLine2 주소 2
 * @param zipCode 우편번호
 * @param country 국가
 * @param deliveryRequest 배송 요청사항
 * @param phoneNumber 연락처
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyReceiverInfoResult(
        String receiverName,
        String receiverPhoneNumber,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber) {

    /** 정적 팩토리 메서드. */
    public static LegacyReceiverInfoResult of(
            String receiverName,
            String receiverPhoneNumber,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber) {
        return new LegacyReceiverInfoResult(
                receiverName,
                receiverPhoneNumber,
                addressLine1,
                addressLine2,
                zipCode,
                country,
                deliveryRequest,
                phoneNumber);
    }
}
