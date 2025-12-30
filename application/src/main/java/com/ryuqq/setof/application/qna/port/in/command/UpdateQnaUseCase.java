package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.UpdateQnaCommand;

/**
 * Update QnA UseCase
 *
 * <p>문의 수정 유스케이스 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateQnaUseCase {

    /**
     * 문의 수정
     *
     * @param command 문의 수정 명령
     */
    void execute(UpdateQnaCommand command);
}
