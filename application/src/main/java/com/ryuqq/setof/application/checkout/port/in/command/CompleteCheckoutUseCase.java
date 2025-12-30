package com.ryuqq.setof.application.checkout.port.in.command;

import com.ryuqq.setof.application.checkout.dto.command.CompleteCheckoutCommand;

/**
 * 체크아웃 완료 UseCase
 *
 * <p>PG 결제 완료 후 체크아웃을 완료 처리합니다.
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>paymentId 기반 분산락 획득
 *   <li>Payment 조회 및 상태 검증
 *   <li>Checkout 조회 및 상태 검증
 *   <li>Redis 재고 차감 (DECRBY)
 *   <li>Payment 승인 처리
 *   <li>판매자별 Order 생성
 *   <li>Checkout 완료 처리
 *   <li>분산락 해제
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CompleteCheckoutUseCase {

    /**
     * 체크아웃 완료 처리
     *
     * @param command 체크아웃 완료 Command (paymentId, pgTransactionId, approvedAmount 포함)
     */
    void completeCheckout(CompleteCheckoutCommand command);
}
