package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.ModifyQnaAnswerCommand;

/**
 * ModifyQnaAnswerUseCase - Q&A 답변 수정 UseCase.
 *
 * <p>레거시 PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId} 기반.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ModifyQnaAnswerUseCase {

    void execute(ModifyQnaAnswerCommand command);
}
