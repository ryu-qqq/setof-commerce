package com.ryuqq.setof.application.productnotice.port.in.command;

import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;

/** 상품고시 등록 UseCase. */
public interface RegisterProductNoticeUseCase {

    /**
     * 상품고시를 등록합니다.
     *
     * @param command 등록 커맨드
     * @return 생성된 상품고시 ID
     */
    Long execute(RegisterProductNoticeCommand command);
}
