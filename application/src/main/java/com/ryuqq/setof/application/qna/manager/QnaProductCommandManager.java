package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.command.QnaProductCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
import org.springframework.stereotype.Component;

/**
 * QnaProductCommandManager - Q&A 상품 매핑 명령 Manager.
 *
 * <p>QnaProductCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaProductCommandManager {

    private final QnaProductCommandPort commandPort;

    public QnaProductCommandManager(QnaProductCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persist(QnaProduct qnaProduct) {
        commandPort.persist(qnaProduct);
    }
}
