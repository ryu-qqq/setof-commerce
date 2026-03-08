package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaAnswerCommand;

/**
 * RegisterQnaAnswerUseCase - Q&A 답변 등록 UseCase.
 *
 * <p>레거시 POST /api/v1/qna/{qnaId}/reply 기반. 답변 등록 시 Q&A 상태가 ANSWERED로 전환됨.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterQnaAnswerUseCase {

    Long execute(RegisterQnaAnswerCommand command);
}
