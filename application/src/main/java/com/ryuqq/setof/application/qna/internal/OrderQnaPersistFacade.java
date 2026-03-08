package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaImageCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaOrderCommandManager;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderQnaPersistFacade - 주문 Q&A 영속 Facade.
 *
 * <p>QnaCommandManager + QnaOrderCommandManager + QnaImageCommandManager를 하나의 @Transactional 경계로
 * 묶습니다. Qna persist 후 반환된 qnaId를 withQnaId()로 QnaOrder에 세팅합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderQnaPersistFacade {

    private final QnaCommandManager qnaCommandManager;
    private final QnaOrderCommandManager orderCommandManager;
    private final QnaImageCommandManager imageCommandManager;

    public OrderQnaPersistFacade(
            QnaCommandManager qnaCommandManager,
            QnaOrderCommandManager orderCommandManager,
            QnaImageCommandManager imageCommandManager) {
        this.qnaCommandManager = qnaCommandManager;
        this.orderCommandManager = orderCommandManager;
        this.imageCommandManager = imageCommandManager;
    }

    @Transactional
    public Long persist(OrderQnaBundle bundle) {
        Long qnaId = qnaCommandManager.persist(bundle.qna());
        LegacyQnaId legacyQnaId = LegacyQnaId.of(qnaId);
        QnaOrder withId = bundle.qnaOrder().withQnaId(legacyQnaId);
        orderCommandManager.persist(withId);
        if (!bundle.images().isEmpty()) {
            imageCommandManager.persistAll(qnaId, bundle.images());
        }
        return qnaId;
    }
}
