package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerAddressCommand;

/** 셀러 주소 수정 UseCase. */
public interface UpdateSellerAddressUseCase {

    void execute(UpdateSellerAddressCommand command);
}
