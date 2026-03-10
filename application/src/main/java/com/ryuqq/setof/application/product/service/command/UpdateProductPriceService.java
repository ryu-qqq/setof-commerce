package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.manager.ProductCommandManager;
import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductPriceUseCase;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
import org.springframework.stereotype.Service;

@Service
public class UpdateProductPriceService implements UpdateProductPriceUseCase {

    private final ProductCommandFactory productCommandFactory;
    private final ProductReadManager productReadManager;
    private final ProductCommandManager productCommandManager;

    public UpdateProductPriceService(
            ProductCommandFactory productCommandFactory,
            ProductReadManager productReadManager,
            ProductCommandManager productCommandManager) {
        this.productCommandFactory = productCommandFactory;
        this.productReadManager = productReadManager;
        this.productCommandManager = productCommandManager;
    }

    @Override
    public void execute(UpdateProductPriceCommand command) {
        StatusChangeContext<ProductId> context =
                productCommandFactory.createPriceUpdateContext(command);
        Product product = productReadManager.getById(context.id());
        product.updatePrice(
                Money.of(command.regularPrice()),
                Money.of(command.currentPrice()),
                context.changedAt());
        productCommandManager.persist(product);
    }
}
