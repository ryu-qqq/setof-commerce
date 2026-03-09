package com.ryuqq.setof.application.discount.port.out.command;

import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;

/**
 * 할인 아웃박스 저장 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountOutboxCommandPort {

    long persist(DiscountOutbox outbox);
}
