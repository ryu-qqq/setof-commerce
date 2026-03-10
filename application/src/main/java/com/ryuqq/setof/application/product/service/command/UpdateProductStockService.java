package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.manager.ProductCommandManager;
import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductStockUseCase;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
import org.springframework.stereotype.Service;

@Service
public class UpdateProductStockService implements UpdateProductStockUseCase {

    private final ProductCommandFactory productCommandFactory;
    private final ProductReadManager productReadManager;
    private final ProductCommandManager productCommandManager;

    public UpdateProductStockService(
            ProductCommandFactory productCommandFactory,
            ProductReadManager productReadManager,
            ProductCommandManager productCommandManager) {
        this.productCommandFactory = productCommandFactory;
        this.productReadManager = productReadManager;
        this.productCommandManager = productCommandManager;
    }

    @Override
    public void execute(UpdateProductStockCommand command) {
        StatusChangeContext<ProductId> context =
                productCommandFactory.createStockUpdateContext(command);
        Product product = productReadManager.getById(context.id());
        product.updateStock(command.stockQuantity(), context.changedAt());
        productCommandManager.persist(product);
    }
}
