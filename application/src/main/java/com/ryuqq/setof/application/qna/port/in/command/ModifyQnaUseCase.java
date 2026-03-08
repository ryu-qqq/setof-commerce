package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;

/**
 * ModifyQnaUseCase - Q&A 질문 수정 UseCase.
 *
 * <p>레거시 PUT /api/v1/qna/{qnaId} 기반.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ModifyQnaUseCase {

    void execute(ModifyQnaCommand command);
}
