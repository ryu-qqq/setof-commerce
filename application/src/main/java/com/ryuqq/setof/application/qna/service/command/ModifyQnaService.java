package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.internal.QnaModificationStrategyProvider;
import com.ryuqq.setof.application.qna.manager.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.command.ModifyQnaUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import org.springframework.stereotype.Service;

/**
 * ModifyQnaService - Q&A 질문 수정 Service.
 *
 * <p>QnaReadManager로 기존 Q&A 조회 → qnaType으로 전략 분기 → Strategy에 수정 위임. 트랜잭션은 각 Strategy 내부에서 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ModifyQnaService implements ModifyQnaUseCase {

    private final QnaReadManager readManager;
    private final QnaModificationStrategyProvider strategyProvider;

    public ModifyQnaService(
            QnaReadManager readManager, QnaModificationStrategyProvider strategyProvider) {
        this.readManager = readManager;
        this.strategyProvider = strategyProvider;
    }

    @Override
    public void execute(ModifyQnaCommand command) {
        Qna existing = readManager.getById(command.qnaId());
        strategyProvider.getStrategy(existing.qnaType()).modify(existing, command);
    }
}
