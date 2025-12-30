package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.CreateQnaReplyCommand;

/**
 * Create QnA Reply UseCase (Command)
 *
 * <p>문의 답변 생성을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateQnaReplyUseCase {

    /**
     * 문의 답변 생성 실행
     *
     * @param command 문의 답변 생성 명령
     * @return 생성된 답변 ID
     */
    Long execute(CreateQnaReplyCommand command);
}
