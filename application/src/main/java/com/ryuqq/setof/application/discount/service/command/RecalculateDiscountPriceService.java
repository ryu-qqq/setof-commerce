package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.internal.DiscountPriceRecalculateProcessor;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.application.discount.port.in.command.RecalculateDiscountPriceUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 할인 가격 재계산 서비스.
 *
 * <p>SQS Consumer가 호출합니다. 아웃박스 상태를 관리하고, 실제 재계산은 프로세서에 위임합니다. 트랜잭션은 프로세서가 관리하며, 아웃박스 상태 저장은 별도
 * 트랜잭션(REQUIRES_NEW)으로 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RecalculateDiscountPriceService implements RecalculateDiscountPriceUseCase {

    private static final Logger log =
            LoggerFactory.getLogger(RecalculateDiscountPriceService.class);

    private final DiscountOutboxReadManager outboxReadManager;
    private final DiscountOutboxCommandManager outboxCommandManager;
    private final DiscountPriceRecalculateProcessor recalculateProcessor;

    public RecalculateDiscountPriceService(
            DiscountOutboxReadManager outboxReadManager,
            DiscountOutboxCommandManager outboxCommandManager,
            DiscountPriceRecalculateProcessor recalculateProcessor) {
        this.outboxReadManager = outboxReadManager;
        this.outboxCommandManager = outboxCommandManager;
        this.recalculateProcessor = recalculateProcessor;
    }

    @Override
    public void execute(long outboxId) {
        DiscountOutbox outbox = outboxReadManager.findById(outboxId).orElse(null);

        if (outbox == null || !outbox.isPublished()) {
            log.warn("아웃박스를 찾을 수 없거나 이미 처리됨: outboxId={}", outboxId);
            return;
        }

        Instant now = Instant.now();

        try {
            recalculateProcessor.process(outbox.targetType(), outbox.targetId());
            outbox.markCompleted(now);
            outboxCommandManager.persist(outbox);

            log.info("가격 재계산 완료: outboxId={}, target={}", outboxId, outbox.targetKeyValue());

        } catch (Exception e) {
            log.error(
                    "가격 재계산 실패: outboxId={}, target={}, error={}",
                    outboxId,
                    outbox.targetKeyValue(),
                    e.getMessage(),
                    e);
            outbox.markFailed("Recalculation failed: " + e.getMessage(), now);
            outboxCommandManager.persist(outbox);
            throw e;
        }
    }
}
