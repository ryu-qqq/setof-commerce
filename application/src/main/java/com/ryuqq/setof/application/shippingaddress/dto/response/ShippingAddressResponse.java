package com.ryuqq.setof.application.shippingaddress.dto.response;

import java.time.Instant;

/**
 * 배송지 정보 응답 DTO
 *
 * <p>배송지 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 배송지 ID
 * @param addressName 배송지 이름
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param roadAddress 도로명 주소
 * @param jibunAddress 지번 주소
 * @param detailAddress 상세 주소
 * @param zipCode 우편번호
 * @param deliveryRequest 배송 요청사항
 * @param isDefault 기본 배송지 여부
 * @param createdAt 생성일시
 * @param modifiedAt 수정일시
 */
public record ShippingAddressResponse(
        Long id,
        String addressName,
        String receiverName,
        String receiverPhone,
        String roadAddress,
        String jibunAddress,
        String detailAddress,
        String zipCode,
        String deliveryRequest,
        boolean isDefault,
        Instant createdAt,
        Instant modifiedAt) {

    /**
     * Static Factory Method
     *
     * @return ShippingAddressResponse 인스턴스
     */
    public static ShippingAddressResponse of(
            Long id,
            String addressName,
            String receiverName,
            String receiverPhone,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String zipCode,
            String deliveryRequest,
            boolean isDefault,
            Instant createdAt,
            Instant modifiedAt) {
        return new ShippingAddressResponse(
                id,
                addressName,
                receiverName,
                receiverPhone,
                roadAddress,
                jibunAddress,
                detailAddress,
                zipCode,
                deliveryRequest,
                isDefault,
                createdAt,
                modifiedAt);
    }
}
