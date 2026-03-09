package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.manager.DiscountOutboxCommandManager;
import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.application.discount.port.in.command.PublishDiscountOutboxUseCase;
import com.ryuqq.setof.application.discount.port.out.client.DiscountOutboxMessageClient;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 할인 아웃박스 SQS 발행 서비스.
 *
 * <p>스케줄러(1초 폴링)가 호출합니다. PENDING 상태의 아웃박스를 조회하여 SQS로 발행하고, 상태를 PUBLISHED로 변경합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class PublishDiscountOutboxService implements PublishDiscountOutboxUseCase {

    private static final Logger log = LoggerFactory.getLogger(PublishDiscountOutboxService.class);

    private final DiscountOutboxReadManager readManager;
    private final DiscountOutboxCommandManager commandManager;
    private final DiscountOutboxMessageClient messageClient;

    public PublishDiscountOutboxService(
            DiscountOutboxReadManager readManager,
            DiscountOutboxCommandManager commandManager,
            DiscountOutboxMessageClient messageClient) {
        this.readManager = readManager;
        this.commandManager = commandManager;
        this.messageClient = messageClient;
    }

    @Override
    @Transactional
    public int execute(int batchSize) {
        List<DiscountOutbox> pendingList = readManager.findPending(batchSize);
        if (pendingList.isEmpty()) {
            return 0;
        }

        int publishedCount = 0;
        Instant now = Instant.now();

        for (DiscountOutbox outbox : pendingList) {
            try {
                messageClient.publish(outbox);
                outbox.markPublished(outbox.targetKeyValue(), now);
                commandManager.persist(outbox);
                publishedCount++;
            } catch (Exception e) {
                log.error(
                        "아웃박스 SQS 발행 실패: outboxId={}, target={}, error={}",
                        outbox.idValue(),
                        outbox.targetKeyValue(),
                        e.getMessage());
                outbox.markFailed("SQS publish failed: " + e.getMessage(), now);
                commandManager.persist(outbox);
            }
        }

        log.info("아웃박스 SQS 발행 완료: total={}, published={}", pendingList.size(), publishedCount);
        return publishedCount;
    }
}
