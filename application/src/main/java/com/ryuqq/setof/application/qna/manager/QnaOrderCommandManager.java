package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.command.QnaOrderCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
import org.springframework.stereotype.Component;

/**
 * QnaOrderCommandManager - Q&A 주문 매핑 명령 Manager.
 *
 * <p>QnaOrderCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaOrderCommandManager {

    private final QnaOrderCommandPort commandPort;

    public QnaOrderCommandManager(QnaOrderCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persist(QnaOrder qnaOrder) {
        commandPort.persist(qnaOrder);
    }
}
