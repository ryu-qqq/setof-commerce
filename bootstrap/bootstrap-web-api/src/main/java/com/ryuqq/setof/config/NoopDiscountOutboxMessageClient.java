package com.ryuqq.setof.config;

import com.ryuqq.setof.application.discount.port.out.client.DiscountOutboxMessageClient;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * SQS 어댑터가 활성화되지 않은 환경(web-api)에서 사용하는 No-op 구현체.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NoopDiscountOutboxMessageClient implements DiscountOutboxMessageClient {

    private static final Logger log =
            LoggerFactory.getLogger(NoopDiscountOutboxMessageClient.class);

    @Override
    public void publish(DiscountOutbox outbox) {
        log.debug(
                "NoopDiscountOutboxMessageClient: publish called but ignored (outboxId={})",
                outbox.id());
    }
}
