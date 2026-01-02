package com.ryuqq.setof.migration.shippingaddress;

import java.time.LocalDateTime;

/**
 * 레거시 SHIPPING_ADDRESS 테이블 조회 DTO
 *
 * @param shippingAddressId 레거시 배송지 ID
 * @param userId 레거시 사용자 ID
 * @param receiverName 수령인 이름
 * @param shippingAddressName 배송지명
 * @param addressLine1 주소 (도로명)
 * @param addressLine2 상세 주소
 * @param zipCode 우편번호
 * @param country 국가
 * @param deliveryRequest 배송 요청사항
 * @param phoneNumber 연락처
 * @param defaultYn 기본 배송지 여부 (Y/N)
 * @param createdAt 생성일시
 * @param modifiedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record LegacyShippingAddressDto(
        Long shippingAddressId,
        Long userId,
        String receiverName,
        String shippingAddressName,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber,
        String defaultYn,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {}
