package com.ryuqq.setof.application.notification.service.command;

import com.ryuqq.setof.application.notification.manager.NotificationOutboxCommandManager;
import com.ryuqq.setof.application.notification.manager.NotificationOutboxReadManager;
import com.ryuqq.setof.application.notification.port.in.command.RecoverStuckNotificationOutboxUseCase;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * RecoverStuckNotificationOutboxService - Stuck 아웃박스 복구 서비스.
 *
 * <p>PUBLISHED 상태에서 5분 이상 머문 아웃박스를 복구합니다. 재시도 횟수가 MAX_RETRY 이상이면 FAILED로, 미만이면 PENDING으로 되돌립니다.
 *
 * <p>스케줄러에서 1분 간격으로 호출됩니다.
 *
 * <p>흐름: Stuck 조회 → 도메인 상태 변경(in-memory) → persist
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RecoverStuckNotificationOutboxService
        implements RecoverStuckNotificationOutboxUseCase {

    private static final Logger log =
            LoggerFactory.getLogger(RecoverStuckNotificationOutboxService.class);
    private static final Duration STUCK_THRESHOLD = Duration.ofMinutes(5);
    private static final int BATCH_SIZE = 50;

    private final NotificationOutboxReadManager readManager;
    private final NotificationOutboxCommandManager commandManager;

    public RecoverStuckNotificationOutboxService(
            NotificationOutboxReadManager readManager,
            NotificationOutboxCommandManager commandManager) {
        this.readManager = readManager;
        this.commandManager = commandManager;
    }

    @Override
    public int execute() {
        Instant now = Instant.now();
        Instant threshold = now.minus(STUCK_THRESHOLD);

        List<NotificationOutbox> stuckList = readManager.findStuckPublished(threshold, BATCH_SIZE);
        if (stuckList.isEmpty()) {
            return 0;
        }

        int recoveredCount = 0;

        for (NotificationOutbox outbox : stuckList) {
            try {
                outbox.recoverStuck(now);
                commandManager.persist(outbox);
                recoveredCount++;

                log.info(
                        "Stuck 아웃박스 복구. id={}, referenceKey={}, newStatus={}",
                        outbox.idValue(),
                        outbox.referenceKey(),
                        outbox.status());
            } catch (Exception e) {
                log.error(
                        "Stuck 아웃박스 복구 실패. id={}, referenceKey={}",
                        outbox.idValue(),
                        outbox.referenceKey(),
                        e);
            }
        }

        log.info("Stuck 아웃박스 복구 완료. total={}, recovered={}", stuckList.size(), recoveredCount);
        return recoveredCount;
    }
}
