package com.ryuqq.setof.application.payment.dto.response;

import java.util.List;

/**
 * PaymentSliceResult - 결제 목록 슬라이스 Application Result DTO.
 *
 * <p>커서 기반 페이징 결과를 포함합니다.
 *
 * <p>APP-DTO-001: Application Result는 Record로 정의.
 *
 * @param content 결제 개요 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param lastPaymentId 마지막 결제 ID (커서)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentSliceResult(
        List<PaymentOverviewResult> content, boolean hasNext, Long lastPaymentId) {}
