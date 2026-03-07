package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 배송지 등록 V1 API Request.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배송지 등록 요청")
public record RegisterShippingAddressV1ApiRequest(
        @Schema(description = "수취인명", example = "홍길동") @NotBlank String receiverName,
        @Schema(description = "배송지명", example = "집") @NotBlank String shippingAddressName,
        @Schema(description = "주소1", example = "서울시 강남구 테헤란로 123") @NotBlank String addressLine1,
        @Schema(description = "주소2", example = "4층 401호") String addressLine2,
        @Schema(description = "우편번호", example = "06234") @NotBlank String zipCode,
        @Schema(description = "국가", example = "KR") @NotNull String country,
        @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요") String deliveryRequest,
        @Schema(description = "전화번호", example = "010-1234-5678") @NotBlank String phoneNumber,
        @Schema(description = "기본 배송지 여부", example = "true") boolean defaultAddress) {}
