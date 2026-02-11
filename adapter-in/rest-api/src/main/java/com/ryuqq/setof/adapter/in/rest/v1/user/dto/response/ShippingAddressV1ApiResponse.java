package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ShippingAddressV1ApiResponse - 배송지 정보 응답 DTO.
 *
 * <p>레거시 UserShippingInfo, ShippingDetails 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param shippingAddressId 배송지 ID
 * @param receiverName 수취인명
 * @param shippingAddressName 배송지명 (예: 집, 회사)
 * @param addressLine1 주소 (시/도, 구/군, 도로명)
 * @param addressLine2 상세주소 (건물, 동호수)
 * @param zipCode 우편번호
 * @param country 국가 코드
 * @param deliveryRequest 배송 요청사항
 * @param phoneNumber 수취인 전화번호
 * @param isDefault 기본 배송지 여부
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.shipping.UserShippingInfo
 */
@Schema(description = "배송지 정보 응답")
public record ShippingAddressV1ApiResponse(
        @Schema(description = "배송지 ID", example = "1") long shippingAddressId,
        @Schema(description = "수취인명", example = "홍길동") String receiverName,
        @Schema(description = "배송지명 (예: 집, 회사)", example = "집") String shippingAddressName,
        @Schema(description = "주소 (시/도, 구/군, 도로명)", example = "서울특별시 강남구 테헤란로 123")
                String addressLine1,
        @Schema(description = "상세주소 (건물, 동호수)", example = "456호") String addressLine2,
        @Schema(description = "우편번호", example = "06234") String zipCode,
        @Schema(
                        description = "국가 코드",
                        example = "KR",
                        allowableValues = {"KR", "US", "JP", "CN"})
                String country,
        @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요", nullable = true)
                String deliveryRequest,
        @Schema(description = "수취인 전화번호", example = "01012345678") String phoneNumber,
        @Schema(description = "기본 배송지 여부", example = "true") boolean isDefault) {}
