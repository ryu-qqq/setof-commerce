package com.ryuqq.setof.storage.legacy.composite.web.user.dto;

/**
 * 레거시 배송지 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
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
public record LegacyWebShippingAddressQueryDto(
        long shippingAddressId,
        String receiverName,
        String shippingAddressName,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber,
        String defaultYn) {}
