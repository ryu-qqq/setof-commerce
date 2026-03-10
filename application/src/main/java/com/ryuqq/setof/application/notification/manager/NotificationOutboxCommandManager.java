package com.ryuqq.setof.application.notification.manager;

import com.ryuqq.setof.application.notification.port.out.command.NotificationOutboxCommandPort;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * NotificationOutboxCommandManager - 알림 아웃박스 Command Manager.
 *
 * <p>APP-MGR-001: Manager는 @Component로 등록.
 *
 * <p>APP-MGR-004: CommandManager는 CommandPort만 의존.
 *
 * <p>APP-MGR-005: @Transactional 필수.
 *
 * <p>persist만 제공. ID 유무에 따라 INSERT/UPDATE 자동 판단 (JPA save 패턴).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NotificationOutboxCommandManager {

    private final NotificationOutboxCommandPort commandPort;

    public NotificationOutboxCommandManager(NotificationOutboxCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    /**
     * 알림 아웃박스 저장.
     *
     * <p>비즈니스 이벤트와 같은 트랜잭션에서 아웃박스를 저장하거나, 스케줄러에서 상태 변경 후 persist합니다.
     *
     * @param outbox 알림 아웃박스 도메인 객체
     */
    @Transactional
    public void persist(NotificationOutbox outbox) {
        commandPort.persist(outbox);
    }
}
