package com.ryuqq.setof.application.discount.port.in.command;

/**
 * 할인 가격 재계산 유스케이스.
 *
 * <p>SQS Consumer에서 호출합니다. 아웃박스 타겟에 해당하는 상품의 가격을 재계산하여 legacy product_group 테이블을 갱신합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RecalculateDiscountPriceUseCase {

    /**
     * 아웃박스 ID로 가격 재계산 수행.
     *
     * @param outboxId 아웃박스 ID
     */
    void execute(long outboxId);
}
