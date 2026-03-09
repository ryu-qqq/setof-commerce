package com.ryuqq.setof.domain.order.vo;

/**
 * 수령인/배송지 정보 VO.
 *
 * <p>Order에 포함되어 주문 시점의 배송 정보를 보관합니다.
 *
 * @param receiverName 수령인명
 * @param receiverPhone 수령인 연락처
 * @param addressLine1 주소1
 * @param addressLine2 주소2 (상세주소)
 * @param zipCode 우편번호
 * @param country 국가
 * @param deliveryRequest 배송 요청사항
 */
public record ReceiverInfo(
        String receiverName,
        String receiverPhone,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest) {

    public ReceiverInfo {
        if (receiverName == null || receiverName.isBlank()) {
            throw new IllegalArgumentException("수령인명은 필수입니다");
        }
        if (addressLine1 == null || addressLine1.isBlank()) {
            throw new IllegalArgumentException("주소는 필수입니다");
        }
    }

    public static ReceiverInfo of(
            String receiverName,
            String receiverPhone,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest) {
        return new ReceiverInfo(
                receiverName,
                receiverPhone,
                addressLine1,
                addressLine2,
                zipCode,
                country,
                deliveryRequest);
    }
}
