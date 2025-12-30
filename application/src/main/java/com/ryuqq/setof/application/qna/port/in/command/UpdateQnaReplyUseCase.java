package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.UpdateQnaReplyCommand;

/**
 * Update QnA Reply UseCase (Command)
 *
 * <p>문의 답변 수정을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateQnaReplyUseCase {

    /**
     * 문의 답변 수정 실행
     *
     * @param command 문의 답변 수정 명령
     */
    void execute(UpdateQnaReplyCommand command);
}
