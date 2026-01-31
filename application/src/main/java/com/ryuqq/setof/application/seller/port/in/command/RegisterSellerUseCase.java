package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;

/** 셀러 등록 UseCase. */
public interface RegisterSellerUseCase {

    Long execute(RegisterSellerCommand command);
}
