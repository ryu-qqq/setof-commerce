package com.ryuqq.setof.application.payment.dto.response;

import java.util.List;

/**
 * 결제 준비(주문서 생성) 결과.
 *
 * <p>클라이언트는 paymentUniqueId로 PG SDK 결제를 진행합니다.
 *
 * @param paymentUniqueId PG 결제용 고유 ID (UUID)
 * @param paymentId 결제 ID (payment 테이블 PK)
 * @param orderIds 생성된 주문 ID 목록 (orders 테이블 PK들)
 * @param expectedMileageAmount 예상 적립 마일리지 (현재 0 고정)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentGatewayResult(
        String paymentUniqueId, long paymentId, List<Long> orderIds, long expectedMileageAmount) {}
