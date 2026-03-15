package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.command.DiscountOutboxCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * DiscountOutboxCommandManager - 할인 아웃박스 저장 매니저.
 *
 * <p>순수 Command(저장) 역할만 수행합니다. 중복 검증은 DiscountOutboxValidator에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountOutboxCommandManager {

    private final DiscountOutboxCommandPort commandPort;

    public DiscountOutboxCommandManager(DiscountOutboxCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void create(DiscountTargetType targetType, long targetId, Instant now) {
        DiscountOutbox outbox = DiscountOutbox.forNew(targetType, targetId, now);
        commandPort.persist(outbox);
    }

    public void persist(DiscountOutbox outbox) {
        commandPort.persist(outbox);
    }
}
