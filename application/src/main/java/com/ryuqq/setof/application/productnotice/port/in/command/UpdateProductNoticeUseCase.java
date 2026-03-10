package com.ryuqq.setof.application.productnotice.port.in.command;

import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;

/** 상품고시 수정 UseCase. */
public interface UpdateProductNoticeUseCase {

    /**
     * 상품고시를 수정합니다.
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateProductNoticeCommand command);
}
