package com.ryuqq.setof.application.shippingaddress.dto.command;

import java.util.UUID;

/**
 * 배송지 등록 Command DTO
 *
 * @param memberId 회원 ID
 * @param addressName 배송지 이름
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param roadAddress 도로명 주소 (nullable)
 * @param jibunAddress 지번 주소 (nullable)
 * @param detailAddress 상세 주소 (nullable)
 * @param zipCode 우편번호
 * @param deliveryRequest 배송 요청사항 (nullable)
 * @param isDefault 기본 배송지 여부
 */
public record RegisterShippingAddressCommand(
        UUID memberId,
        String addressName,
        String receiverName,
        String receiverPhone,
        String roadAddress,
        String jibunAddress,
        String detailAddress,
        String zipCode,
        String deliveryRequest,
        boolean isDefault) {

    /** Static Factory Method */
    public static RegisterShippingAddressCommand of(
            UUID memberId,
            String addressName,
            String receiverName,
            String receiverPhone,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String zipCode,
            String deliveryRequest,
            boolean isDefault) {
        return new RegisterShippingAddressCommand(
                memberId,
                addressName,
                receiverName,
                receiverPhone,
                roadAddress,
                jibunAddress,
                detailAddress,
                zipCode,
                deliveryRequest,
                isDefault);
    }
}
