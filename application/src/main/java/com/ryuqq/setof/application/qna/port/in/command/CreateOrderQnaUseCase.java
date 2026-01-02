package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.CreateOrderQnaCommand;

/**
 * Create Order QnA UseCase (Command)
 *
 * <p>주문 문의 생성을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateOrderQnaUseCase {

    /**
     * 주문 문의 생성 실행
     *
     * @param command 주문 문의 생성 명령
     * @return 생성된 문의 ID
     */
    Long execute(CreateOrderQnaCommand command);
}
