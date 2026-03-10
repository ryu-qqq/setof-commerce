package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;

public interface UpdateProductsUseCase {
    void execute(UpdateProductsCommand command);
}
