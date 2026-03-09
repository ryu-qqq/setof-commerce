package com.ryuqq.setof.application.payment.dto.response;

/**
 * PaymentMethodResult - 결제 수단 조회 결과 DTO.
 *
 * <p>레거시 payment_method + common_code 테이블에서 조회한 결과를 담습니다.
 *
 * <p>CommonCode 도메인에는 merchantKey 필드가 없으므로 별도 Result로 분리합니다.
 *
 * @param code 결제 수단 코드 (예: CARD, KAKAO_PAY)
 * @param displayName 결제 수단 표시명 (예: 신용/체크카드)
 * @param merchantKey PG 가맹점 키
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentMethodResult(String code, String displayName, String merchantKey) {}
