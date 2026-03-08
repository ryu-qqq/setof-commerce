package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.command.QnaCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import org.springframework.stereotype.Component;

/**
 * QnaCommandManager - Q&A 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaCommandManager {

    private final QnaCommandPort commandPort;

    public QnaCommandManager(QnaCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(Qna qna) {
        return commandPort.persist(qna);
    }
}
