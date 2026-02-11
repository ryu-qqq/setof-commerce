package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PayMethodV1ApiResponse - 결제 수단 응답 DTO.
 *
 * <p>레거시 PayMethodResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param displayName 결제 수단 표시명
 * @param payMethod 결제 방법 코드
 * @param paymentMethodMerchantKey 가맹점 키
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.payment.dto.paymethod.PayMethodResponse
 */
@Schema(description = "결제 수단 응답")
public record PayMethodV1ApiResponse(
        @Schema(description = "결제 수단 표시명", example = "신용카드") String displayName,
        @Schema(
                        description = "결제 방법 코드",
                        example = "CARD",
                        allowableValues = {
                            "CARD",
                            "VBANK",
                            "PHONE",
                            "KAKAOPAY",
                            "NAVERPAY",
                            "TOSSPAY",
                            "MILEAGE"
                        })
                String payMethod,
        @Schema(description = "가맹점 키", example = "imp12345678") String paymentMethodMerchantKey) {}
