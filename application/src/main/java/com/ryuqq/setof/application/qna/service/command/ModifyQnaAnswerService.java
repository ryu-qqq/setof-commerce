package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.ModifyQnaAnswerCommand;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.command.ModifyQnaAnswerUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * ModifyQnaAnswerService - Q&A 답변 수정 Service.
 *
 * <p>ReadManager로 Q&A 조회 → editAnswer 호출 → CommandManager로 persist. Qna Aggregate 단위로 qna + answer가
 * 함께 저장됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ModifyQnaAnswerService implements ModifyQnaAnswerUseCase {

    private final QnaReadManager readManager;
    private final QnaCommandManager commandManager;

    public ModifyQnaAnswerService(QnaReadManager readManager, QnaCommandManager commandManager) {
        this.readManager = readManager;
        this.commandManager = commandManager;
    }

    @Override
    public void execute(ModifyQnaAnswerCommand command) {
        Qna qna = readManager.getById(command.qnaId());

        Instant now = Instant.now();
        qna.editAnswer(QnaContent.of(command.content()), now);

        commandManager.persist(qna);
    }
}
