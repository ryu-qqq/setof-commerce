package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.query.QnaProductPendingQueryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnaProductPendingReadManager - 상품 Q&A 미답변 질문 조회 Manager.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaProductPendingReadManager {

    private final QnaProductPendingQueryPort queryPort;

    public QnaProductPendingReadManager(QnaProductPendingQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public boolean existsPendingProductQna(long userId, long productGroupId) {
        return queryPort.existsPendingProductQna(userId, productGroupId);
    }
}
