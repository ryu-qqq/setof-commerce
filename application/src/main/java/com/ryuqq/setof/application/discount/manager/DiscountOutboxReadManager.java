package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.query.DiscountOutboxQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 할인 아웃박스 조회 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountOutboxReadManager {

    private final DiscountOutboxQueryPort queryPort;

    public DiscountOutboxReadManager(DiscountOutboxQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public Optional<DiscountOutbox> findById(long id) {
        return queryPort.findById(id);
    }

    public List<DiscountOutbox> findPending(int batchSize) {
        return queryPort.findByStatus(OutboxStatus.PENDING, batchSize);
    }

    public List<DiscountOutbox> findStuckPublished(long timeoutSeconds, int batchSize) {
        Instant before = Instant.now().minusSeconds(timeoutSeconds);
        return queryPort.findStuckPublished(before, batchSize);
    }

    public boolean existsPendingForTarget(DiscountTargetType targetType, long targetId) {
        return queryPort.existsByTargetAndStatus(targetType, targetId, OutboxStatus.PENDING);
    }

    public boolean existsPublishedForTarget(DiscountTargetType targetType, long targetId) {
        return queryPort.existsByTargetAndStatus(targetType, targetId, OutboxStatus.PUBLISHED);
    }
}
