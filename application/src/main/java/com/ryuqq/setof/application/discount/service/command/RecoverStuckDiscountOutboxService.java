package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.application.discount.port.in.command.RecoverStuckDiscountOutboxUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Stuck 아웃박스 복구 서비스.
 *
 * <p>PUBLISHED 상태로 일정 시간 이상 머문 항목을 PENDING으로 복구합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RecoverStuckDiscountOutboxService implements RecoverStuckDiscountOutboxUseCase {

    private static final Logger log =
            LoggerFactory.getLogger(RecoverStuckDiscountOutboxService.class);

    private final DiscountOutboxReadManager readManager;
    private final DiscountOutboxCommandManager commandManager;

    public RecoverStuckDiscountOutboxService(
            DiscountOutboxReadManager readManager, DiscountOutboxCommandManager commandManager) {
        this.readManager = readManager;
        this.commandManager = commandManager;
    }

    @Override
    @Transactional
    public int execute(long timeoutSeconds, int batchSize) {
        List<DiscountOutbox> stuckList = readManager.findStuckPublished(timeoutSeconds, batchSize);
        if (stuckList.isEmpty()) {
            return 0;
        }

        Instant now = Instant.now();
        int recoveredCount = 0;

        for (DiscountOutbox outbox : stuckList) {
            outbox.recoverStuck(now);
            commandManager.persist(outbox);
            recoveredCount++;

            if (outbox.isMaxRetryExceeded()) {
                log.warn(
                        "아웃박스 최대 재시도 초과로 FAILED 전환: outboxId={}, target={}",
                        outbox.idValue(),
                        outbox.targetKeyValue());
            } else {
                log.info(
                        "아웃박스 stuck 복구 (PENDING): outboxId={}, target={}, retryCount={}",
                        outbox.idValue(),
                        outbox.targetKeyValue(),
                        outbox.retryCount());
            }
        }

        log.info("Stuck 아웃박스 복구 완료: total={}, recovered={}", stuckList.size(), recoveredCount);
        return recoveredCount;
    }
}
