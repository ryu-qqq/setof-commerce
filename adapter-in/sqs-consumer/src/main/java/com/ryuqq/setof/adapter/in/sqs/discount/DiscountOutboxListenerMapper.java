package com.ryuqq.setof.adapter.in.sqs.discount;

import com.ryuqq.setof.application.discount.dto.messaging.DiscountOutboxPayload;
import org.springframework.stereotype.Component;

/**
 * Discount Outbox 리스너 매퍼.
 *
 * <p>DiscountOutboxPayload에서 outboxId를 추출합니다. 실제 재계산은 outboxId 기반으로 Application Layer에서 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountOutboxListenerMapper {

    public long toOutboxId(DiscountOutboxPayload payload) {
        return payload.outboxId();
    }
}
