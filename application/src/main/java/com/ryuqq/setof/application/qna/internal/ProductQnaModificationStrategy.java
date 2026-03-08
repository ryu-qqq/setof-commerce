package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.exception.QnaImageNotAllowedException;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductQnaModificationStrategy - 상품 Q&A 수정 전략.
 *
 * <p>상품 Q&A는 이미지 수정이 불가합니다. Factory → UpdateContext → qna.update() → persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductQnaModificationStrategy implements QnaModificationStrategy {

    private final QnaCommandFactory factory;
    private final QnaCommandManager commandManager;

    public ProductQnaModificationStrategy(
            QnaCommandFactory factory, QnaCommandManager commandManager) {
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Override
    public QnaType supportType() {
        return QnaType.PRODUCT;
    }

    @Override
    @Transactional
    public void modify(Qna qna, ModifyQnaCommand command) {
        if (!command.imageUrls().isEmpty()) {
            throw new QnaImageNotAllowedException();
        }

        UpdateContext<Long, QnaUpdateData> context = factory.createUpdateContext(command);
        qna.update(context.updateData(), context.changedAt());
        commandManager.persist(qna);
    }
}
