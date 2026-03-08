package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaProductCommandManager;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductQnaPersistFacade - 상품 Q&A 영속 Facade.
 *
 * <p>QnaCommandManager + QnaProductCommandManager를 하나의 @Transactional 경계로 묶습니다. Qna persist 후 반환된
 * qnaId를 withQnaId()로 QnaProduct에 세팅합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductQnaPersistFacade {

    private final QnaCommandManager qnaCommandManager;
    private final QnaProductCommandManager productCommandManager;

    public ProductQnaPersistFacade(
            QnaCommandManager qnaCommandManager, QnaProductCommandManager productCommandManager) {
        this.qnaCommandManager = qnaCommandManager;
        this.productCommandManager = productCommandManager;
    }

    @Transactional
    public Long persist(ProductQnaBundle bundle) {
        Long qnaId = qnaCommandManager.persist(bundle.qna());
        QnaProduct withId = bundle.qnaProduct().withQnaId(LegacyQnaId.of(qnaId));
        productCommandManager.persist(withId);
        return qnaId;
    }
}
