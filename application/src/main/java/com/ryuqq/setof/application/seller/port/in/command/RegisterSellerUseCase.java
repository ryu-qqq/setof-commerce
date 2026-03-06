package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;

public interface RegisterSellerUseCase {
    Long execute(RegisterSellerCommand command);
}
