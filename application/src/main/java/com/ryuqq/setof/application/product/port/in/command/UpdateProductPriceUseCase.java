package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;

public interface UpdateProductPriceUseCase {
    void execute(UpdateProductPriceCommand command);
}
