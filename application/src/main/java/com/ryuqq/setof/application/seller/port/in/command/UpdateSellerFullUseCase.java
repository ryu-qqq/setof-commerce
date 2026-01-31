package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerFullCommand;

/** 셀러 전체 수정 UseCase. */
public interface UpdateSellerFullUseCase {

    void execute(UpdateSellerFullCommand command);
}
