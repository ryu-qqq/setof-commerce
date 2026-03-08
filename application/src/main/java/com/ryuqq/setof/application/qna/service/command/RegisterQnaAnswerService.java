package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaAnswerCommand;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.command.RegisterQnaAnswerUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * RegisterQnaAnswerService - Q&A 답변 등록 Service.
 *
 * <p>ReadManager로 Q&A 조회 → registerAnswer 호출 → CommandManager로 persist. Qna Aggregate 단위로 qna +
 * answer가 함께 저장됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterQnaAnswerService implements RegisterQnaAnswerUseCase {

    private final QnaReadManager readManager;
    private final QnaCommandManager commandManager;

    public RegisterQnaAnswerService(QnaReadManager readManager, QnaCommandManager commandManager) {
        this.readManager = readManager;
        this.commandManager = commandManager;
    }

    @Override
    public Long execute(RegisterQnaAnswerCommand command) {
        Qna qna = readManager.getById(command.qnaId());

        Instant now = Instant.now();
        qna.registerAnswer(QnaContent.of(command.content()), now);

        return commandManager.persist(qna);
    }
}
