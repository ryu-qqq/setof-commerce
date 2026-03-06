package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;

public interface UpdateSellerUseCase {
    void execute(UpdateSellerCommand command);
}
