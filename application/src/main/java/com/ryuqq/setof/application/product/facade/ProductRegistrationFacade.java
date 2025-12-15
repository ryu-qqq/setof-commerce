package com.ryuqq.setof.application.product.facade;

import com.ryuqq.setof.application.product.dto.bundle.ProductSubAggregatesPersistBundle;
import com.ryuqq.setof.application.product.dto.command.ProductSkuCommandDto;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand.RegisterProductCommand;
import com.ryuqq.setof.application.product.factory.command.ProductGroupCommandFactory;
import com.ryuqq.setof.application.product.factory.command.ProductSubAggregatesBundleFactory;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.command.ProductSubAggregatesPersistenceManager;
import com.ryuqq.setof.application.productstock.dto.command.InitializeStockCommand;
import com.ryuqq.setof.application.productstock.factory.command.ProductStockCommandFactory;
import com.ryuqq.setof.application.productstock.manager.command.ProductStockPersistenceManager;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품 등록 Facade
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 + 재고 일괄 등록을 조율합니다.
 *
 * <p>트랜잭션 없음 - Service에서 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductRegistrationFacade {

    private final ProductGroupCommandFactory productGroupFactory;
    private final ProductGroupPersistenceManager productGroupManager;
    private final ProductSubAggregatesBundleFactory subAggregatesBundleFactory;
    private final ProductSubAggregatesPersistenceManager subAggregatesManager;
    private final ProductStockCommandFactory stockFactory;
    private final ProductStockPersistenceManager stockManager;

    public ProductRegistrationFacade(
            ProductGroupCommandFactory productGroupFactory,
            ProductGroupPersistenceManager productGroupManager,
            ProductSubAggregatesBundleFactory subAggregatesBundleFactory,
            ProductSubAggregatesPersistenceManager subAggregatesManager,
            ProductStockCommandFactory stockFactory,
            ProductStockPersistenceManager stockManager) {
        this.productGroupFactory = productGroupFactory;
        this.productGroupManager = productGroupManager;
        this.subAggregatesBundleFactory = subAggregatesBundleFactory;
        this.subAggregatesManager = subAggregatesManager;
        this.stockFactory = stockFactory;
        this.stockManager = stockManager;
    }

    /**
     * 전체 상품 등록 (트랜잭션 없음 - Service에서 관리)
     *
     * @param command 전체 등록 Command
     * @return 생성된 ProductGroup ID
     */
    public Long registerAll(RegisterFullProductCommand command) {
        Long productGroupIdValue = persistProductGroup(command);
        List<Long> productIds = persistSubAggregates(productGroupIdValue, command);
        List<Integer> initialStocks = extractInitialStocks(command);
        initializeStocks(productIds, initialStocks);
        return productGroupIdValue;
    }

    private Long persistProductGroup(RegisterFullProductCommand command) {
        RegisterProductGroupCommand groupCommand = toProductGroupCommand(command);
        ProductGroup productGroup = productGroupFactory.createProductGroup(groupCommand);
        ProductGroupId productGroupId = productGroupManager.persist(productGroup);
        return productGroupId.value();
    }

    private List<Long> persistSubAggregates(
            Long productGroupId, RegisterFullProductCommand command) {
        ProductSubAggregatesPersistBundle bundle =
                subAggregatesBundleFactory.create(productGroupId, command);
        return subAggregatesManager.persist(bundle).stream()
                .map(productId -> productId.value())
                .toList();
    }

    private List<Integer> extractInitialStocks(RegisterFullProductCommand command) {
        return command.products().stream().map(ProductSkuCommandDto::initialStock).toList();
    }

    /**
     * 재고 초기화 (Product ID 목록 필요)
     *
     * @param productIds Product ID 목록
     * @param quantities 각 Product별 초기 재고 수량
     */
    public void initializeStocks(List<Long> productIds, List<Integer> quantities) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int quantity = (quantities != null && i < quantities.size()) ? quantities.get(i) : 0;

            InitializeStockCommand stockCommand = new InitializeStockCommand(productId, quantity);
            ProductStock productStock = stockFactory.createProductStock(stockCommand);
            stockManager.persist(productStock);
        }
    }

    private RegisterProductGroupCommand toProductGroupCommand(RegisterFullProductCommand command) {
        List<RegisterProductCommand> products =
                command.products().stream().map(this::toRegisterProductCommand).toList();

        return new RegisterProductGroupCommand(
                command.sellerId(),
                command.categoryId(),
                command.brandId(),
                command.name(),
                command.optionType(),
                command.regularPrice(),
                command.currentPrice(),
                command.shippingPolicyId(),
                command.refundPolicyId(),
                products);
    }

    private RegisterProductCommand toRegisterProductCommand(ProductSkuCommandDto sku) {
        return new RegisterProductCommand(
                sku.option1Name(),
                sku.option1Value(),
                sku.option2Name(),
                sku.option2Value(),
                sku.additionalPrice(),
                sku.initialStock());
    }
}
