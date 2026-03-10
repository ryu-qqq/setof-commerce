package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;

public interface UpdateProductStockUseCase {
    void execute(UpdateProductStockCommand command);
}
