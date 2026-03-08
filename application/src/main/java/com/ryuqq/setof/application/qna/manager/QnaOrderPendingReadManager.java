package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.query.QnaOrderPendingQueryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnaOrderPendingReadManager - 주문 Q&A 미답변 질문 조회 Manager.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaOrderPendingReadManager {

    private final QnaOrderPendingQueryPort queryPort;

    public QnaOrderPendingReadManager(QnaOrderPendingQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public boolean existsPendingOrderQna(long userId, long legacyOrderId) {
        return queryPort.existsPendingOrderQna(userId, legacyOrderId);
    }
}
