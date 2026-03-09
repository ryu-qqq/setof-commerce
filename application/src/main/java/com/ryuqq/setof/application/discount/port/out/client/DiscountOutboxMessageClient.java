package com.ryuqq.setof.application.discount.port.out.client;

import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;

/**
 * 할인 아웃박스 SQS 메시지 발행 클라이언트 포트.
 *
 * <p>adapter-out/client/sqs-client에서 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountOutboxMessageClient {

    /**
     * 아웃박스 항목을 SQS로 발행.
     *
     * @param outbox 발행할 아웃박스
     */
    void publish(DiscountOutbox outbox);
}
