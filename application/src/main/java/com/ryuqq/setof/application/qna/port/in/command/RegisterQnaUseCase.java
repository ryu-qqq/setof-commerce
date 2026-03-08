package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;

/**
 * RegisterQnaUseCase - Q&A 질문 등록 UseCase.
 *
 * <p>레거시 POST /api/v1/qna 기반. PRODUCT/ORDER 분기 처리, 이미지 첨부 지원.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterQnaUseCase {

    Long execute(RegisterQnaCommand command);
}
