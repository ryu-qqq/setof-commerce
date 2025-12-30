package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.MarkProductOutOfStockCommand;
import com.ryuqq.setof.application.product.manager.command.ProductWriteManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.manager.query.ProductSkuReadManager;
import com.ryuqq.setof.application.product.port.in.command.MarkProductOutOfStockUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 상품(SKU) 품절 상태 변경 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductSkuReadManager로 기존 Product 조회
 *   <li>ProductGroup을 통해 셀러 소유권 검증
 *   <li>품절 상태 변경 (markSoldOut/markInStock)
 *   <li>ProductWriteManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class MarkProductOutOfStockService implements MarkProductOutOfStockUseCase {

    private final ProductSkuReadManager productSkuReadManager;
    private final ProductGroupReadManager productGroupReadManager;
    private final ProductWriteManager productWriteManager;
    private final ClockHolder clockHolder;

    public MarkProductOutOfStockService(
            ProductSkuReadManager productSkuReadManager,
            ProductGroupReadManager productGroupReadManager,
            ProductWriteManager productWriteManager,
            ClockHolder clockHolder) {
        this.productSkuReadManager = productSkuReadManager;
        this.productGroupReadManager = productGroupReadManager;
        this.productWriteManager = productWriteManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(MarkProductOutOfStockCommand command) {
        Product product = productSkuReadManager.findById(command.productId());

        // ProductGroup을 통해 셀러 소유권 검증
        ProductGroup productGroup =
                productGroupReadManager.findById(product.getProductGroupIdValue());
        validateOwnership(productGroup, command.sellerId());

        Instant now = Instant.now(clockHolder.getClock());
        Product updated = command.soldOut() ? product.markSoldOut(now) : product.markInStock(now);

        productWriteManager.save(updated);
    }

    private void validateOwnership(ProductGroup productGroup, Long sellerId) {
        if (!productGroup.getSellerIdValue().equals(sellerId)) {
            throw new IllegalArgumentException("해당 상품의 품절 상태 변경 권한이 없습니다");
        }
    }
}
