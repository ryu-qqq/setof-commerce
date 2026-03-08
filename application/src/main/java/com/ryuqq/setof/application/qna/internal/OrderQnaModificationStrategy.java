package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaImageCommandManager;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderQnaModificationStrategy - 주문 Q&A 수정 전략.
 *
 * <p>주문 Q&A는 이미지 교체가 가능합니다. Factory → UpdateContext → qna.update() + 이미지 diff(전체 교체) → persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderQnaModificationStrategy implements QnaModificationStrategy {

    private final QnaCommandManager commandManager;
    private final QnaImageCommandManager imageCommandManager;
    private final QnaCommandFactory factory;

    public OrderQnaModificationStrategy(
            QnaCommandManager commandManager,
            QnaImageCommandManager imageCommandManager,
            QnaCommandFactory factory) {
        this.commandManager = commandManager;
        this.imageCommandManager = imageCommandManager;
        this.factory = factory;
    }

    @Override
    public QnaType supportType() {
        return QnaType.ORDER;
    }

    @Override
    @Transactional
    public void modify(Qna qna, ModifyQnaCommand command) {
        UpdateContext<Long, QnaUpdateData> context = factory.createUpdateContext(command);
        qna.update(context.updateData(), context.changedAt());
        Long qnaId = commandManager.persist(qna);

        if (!command.imageUrls().isEmpty()) {
            imageCommandManager.deleteAllByQnaId(qnaId);
            QnaImages newImages = factory.createImages(command.imageUrls());
            imageCommandManager.persistAll(qnaId, newImages);
        }
    }
}
