package com.ryuqq.setof.application.product.facade;

import com.ryuqq.setof.application.product.dto.command.UpdateProductOptionCommand;
import com.ryuqq.setof.application.product.manager.command.ProductWriteManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.manager.query.ProductSkuReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductOptionUseCase;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.Money;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품(SKU) 옵션 수정 Facade
 *
 * <p>Product 옵션 수정과 ProductStock 수정을 조율
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductSkuReadManager로 기존 Product 조회
 *   <li>ProductGroup을 통해 셀러 소유권 검증
 *   <li>옵션 정보 수정 (Product.updateOption)
 *   <li>재고 수량 수정 (SetStockUseCase - quantity 있는 경우)
 *   <li>ProductWriteManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class UpdateProductOptionFacade implements UpdateProductOptionUseCase {

    private final ProductSkuReadManager productSkuReadManager;
    private final ProductGroupReadManager productGroupReadManager;
    private final ProductWriteManager productWriteManager;
    private final SetStockUseCase setStockUseCase;
    private final ClockHolder clockHolder;

    public UpdateProductOptionFacade(
            ProductSkuReadManager productSkuReadManager,
            ProductGroupReadManager productGroupReadManager,
            ProductWriteManager productWriteManager,
            SetStockUseCase setStockUseCase,
            ClockHolder clockHolder) {
        this.productSkuReadManager = productSkuReadManager;
        this.productGroupReadManager = productGroupReadManager;
        this.productWriteManager = productWriteManager;
        this.setStockUseCase = setStockUseCase;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void execute(UpdateProductOptionCommand command) {
        Product product = productSkuReadManager.findById(command.productId());

        // ProductGroup을 통해 셀러 소유권 검증
        ProductGroup productGroup =
                productGroupReadManager.findById(product.getProductGroupIdValue());
        validateOwnership(productGroup, command.sellerId());

        Instant now = Instant.now(clockHolder.getClock());

        // 옵션 정보 수정
        Money additionalPrice =
                command.additionalPrice() != null ? Money.of(command.additionalPrice()) : null;

        Product updated =
                product.updateOption(
                        command.option1Name(),
                        command.option1Value(),
                        command.option2Name(),
                        command.option2Value(),
                        additionalPrice,
                        now);

        productWriteManager.save(updated);

        // 재고 수량 수정 (quantity가 있는 경우)
        if (command.quantity() != null) {
            SetStockCommand stockCommand =
                    new SetStockCommand(command.productId(), command.quantity());
            setStockUseCase.execute(stockCommand);
        }
    }

    private void validateOwnership(ProductGroup productGroup, Long sellerId) {
        if (!productGroup.getSellerIdValue().equals(sellerId)) {
            throw new IllegalArgumentException("해당 상품의 옵션 수정 권한이 없습니다");
        }
    }
}
