package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;

/** 셀러 기본정보 수정 UseCase. */
public interface UpdateSellerUseCase {

    void execute(UpdateSellerCommand command);
}
