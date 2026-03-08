package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.query.QnaOrderPendingQueryPort;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaOrderPendingQueryDslRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaOrderPendingQueryAdapter - 주문 Q&A 미답변 질문 조회 Adapter.
 *
 * <p>Application Layer의 QnaOrderPendingQueryPort를 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaOrderPendingQueryAdapter implements QnaOrderPendingQueryPort {

    private final LegacyQnaOrderPendingQueryDslRepository repository;

    public LegacyQnaOrderPendingQueryAdapter(LegacyQnaOrderPendingQueryDslRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsPendingOrderQna(long userId, long legacyOrderId) {
        return repository.existsPendingOrderQna(userId, legacyOrderId);
    }
}
