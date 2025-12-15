package com.ryuqq.setof.application.productnotice.port.in.command;

import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;

/**
 * 상품고시 수정 UseCase
 *
 * <p>상품고시를 수정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductNoticeUseCase {

    /**
     * 상품고시 수정
     *
     * @param command 수정 Command
     */
    void execute(UpdateProductNoticeCommand command);
}
