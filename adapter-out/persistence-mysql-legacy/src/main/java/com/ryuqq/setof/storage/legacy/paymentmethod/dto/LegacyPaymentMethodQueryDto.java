package com.ryuqq.setof.storage.legacy.paymentmethod.dto;

/**
 * LegacyPaymentMethodQueryDto - 결제 수단 QueryDSL 조회 DTO.
 *
 * <p>payment_method + common_code JOIN 결과를 Projections.constructor로 매핑합니다.
 *
 * @param paymentMethod 결제 수단 코드 (예: CARD, KAKAO_PAY)
 * @param displayName 결제 수단 표시명 (common_code.CODE_DETAIL_DISPLAY_NAME)
 * @param merchantKey PG 가맹점 키 (payment_method.PAYMENT_METHOD_MERCHANT_KEY)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyPaymentMethodQueryDto(
        String paymentMethod, String displayName, String merchantKey) {}
