package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.CreateProductQnaCommand;

/**
 * Create Product QnA UseCase (Command)
 *
 * <p>상품 문의 생성을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateProductQnaUseCase {

    /**
     * 상품 문의 생성 실행
     *
     * @param command 상품 문의 생성 명령
     * @return 생성된 문의 ID
     */
    Long execute(CreateProductQnaCommand command);
}
