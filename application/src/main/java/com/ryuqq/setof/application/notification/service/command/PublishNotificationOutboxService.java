package com.ryuqq.setof.application.notification.service.command;

import com.ryuqq.setof.application.notification.manager.NotificationOutboxCommandManager;
import com.ryuqq.setof.application.notification.manager.NotificationOutboxReadManager;
import com.ryuqq.setof.application.notification.port.in.command.PublishNotificationOutboxUseCase;
import com.ryuqq.setof.application.notification.port.out.client.NotificationOutboxMessageClient;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * PublishNotificationOutboxService - PENDING 아웃박스를 SQS로 발행하는 서비스.
 *
 * <p>스케줄러에서 5초 간격으로 호출됩니다. 각 아웃박스를 개별적으로 처리하여, 하나의 실패가 다른 건에 영향을 주지 않습니다.
 *
 * <p>흐름: PENDING 조회 → SQS 발행 → 도메인 상태 변경(in-memory) → persist
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class PublishNotificationOutboxService implements PublishNotificationOutboxUseCase {

    private static final Logger log =
            LoggerFactory.getLogger(PublishNotificationOutboxService.class);
    private static final int BATCH_SIZE = 50;

    private final NotificationOutboxReadManager readManager;
    private final NotificationOutboxCommandManager commandManager;
    private final NotificationOutboxMessageClient messageClient;

    public PublishNotificationOutboxService(
            NotificationOutboxReadManager readManager,
            NotificationOutboxCommandManager commandManager,
            NotificationOutboxMessageClient messageClient) {
        this.readManager = readManager;
        this.commandManager = commandManager;
        this.messageClient = messageClient;
    }

    @Override
    public int execute() {
        List<NotificationOutbox> pendingList = readManager.findPending(BATCH_SIZE);
        if (pendingList.isEmpty()) {
            return 0;
        }

        int publishedCount = 0;
        Instant now = Instant.now();

        for (NotificationOutbox outbox : pendingList) {
            try {
                messageClient.publish(outbox);
                outbox.markPublished(now);
                publishedCount++;
            } catch (Exception e) {
                log.error(
                        "알림 아웃박스 SQS 발행 실패. id={}, referenceKey={}",
                        outbox.idValue(),
                        outbox.referenceKey(),
                        e);
                outbox.markFailed("SQS 발행 실패: " + e.getMessage(), now);
            }
            commandManager.persist(outbox);
        }

        log.info("알림 아웃박스 발행 완료. total={}, published={}", pendingList.size(), publishedCount);
        return publishedCount;
    }
}
