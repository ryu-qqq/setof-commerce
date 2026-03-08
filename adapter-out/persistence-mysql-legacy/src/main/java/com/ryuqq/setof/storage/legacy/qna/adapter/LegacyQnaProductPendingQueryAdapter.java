package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.query.QnaProductPendingQueryPort;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaProductPendingQueryDslRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaProductPendingQueryAdapter - 상품 Q&A 미답변 질문 조회 Adapter.
 *
 * <p>Application Layer의 QnaProductPendingQueryPort를 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaProductPendingQueryAdapter implements QnaProductPendingQueryPort {

    private final LegacyQnaProductPendingQueryDslRepository repository;

    public LegacyQnaProductPendingQueryAdapter(
            LegacyQnaProductPendingQueryDslRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsPendingProductQna(long userId, long productGroupId) {
        return repository.existsPendingProductQna(userId, productGroupId);
    }
}
