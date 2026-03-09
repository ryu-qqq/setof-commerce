package com.ryuqq.setof.application.discount.port.in.command;

/**
 * 할인 아웃박스 SQS 발행 유스케이스.
 *
 * <p>스케줄러에서 호출합니다. PENDING 상태의 아웃박스를 조회하여 SQS로 발행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PublishDiscountOutboxUseCase {

    /**
     * PENDING 아웃박스를 배치 크기만큼 조회하여 SQS로 발행.
     *
     * @param batchSize 배치 크기
     * @return 발행된 건수
     */
    int execute(int batchSize);
}
