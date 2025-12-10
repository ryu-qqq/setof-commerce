package com.ryuqq.setof.application.shippingaddress.dto.command;

import java.util.UUID;

/**
 * 배송지 수정 Command DTO
 *
 * @param memberId 회원 ID (소유권 검증용)
 * @param shippingAddressId 배송지 ID
 * @param addressName 배송지 이름
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param roadAddress 도로명 주소 (nullable)
 * @param jibunAddress 지번 주소 (nullable)
 * @param detailAddress 상세 주소 (nullable)
 * @param zipCode 우편번호
 * @param deliveryRequest 배송 요청사항 (nullable)
 */
public record UpdateShippingAddressCommand(
        UUID memberId,
        Long shippingAddressId,
        String addressName,
        String receiverName,
        String receiverPhone,
        String roadAddress,
        String jibunAddress,
        String detailAddress,
        String zipCode,
        String deliveryRequest) {

    /** Static Factory Method */
    public static UpdateShippingAddressCommand of(
            UUID memberId,
            Long shippingAddressId,
            String addressName,
            String receiverName,
            String receiverPhone,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String zipCode,
            String deliveryRequest) {
        return new UpdateShippingAddressCommand(
                memberId,
                shippingAddressId,
                addressName,
                receiverName,
                receiverPhone,
                roadAddress,
                jibunAddress,
                detailAddress,
                zipCode,
                deliveryRequest);
    }
}
