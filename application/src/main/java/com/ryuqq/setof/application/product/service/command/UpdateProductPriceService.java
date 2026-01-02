package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.factory.command.ProductGroupCommandFactory;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductPriceUseCase;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.Price;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 상품 가격 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductGroupReadManager로 기존 ProductGroup 조회
 *   <li>셀러 소유권 검증 (sellerId가 있는 경우)
 *   <li>가격 유효성 검증 (정가 >= 판매가)
 *   <li>도메인 메서드로 가격 수정
 *   <li>ProductGroupPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateProductPriceService implements UpdateProductPriceUseCase {

    private final ProductGroupReadManager productGroupReadManager;
    private final ProductGroupPersistenceManager productGroupPersistenceManager;
    private final ProductGroupCommandFactory productGroupCommandFactory;

    public UpdateProductPriceService(
            ProductGroupReadManager productGroupReadManager,
            ProductGroupPersistenceManager productGroupPersistenceManager,
            ProductGroupCommandFactory productGroupCommandFactory) {
        this.productGroupReadManager = productGroupReadManager;
        this.productGroupPersistenceManager = productGroupPersistenceManager;
        this.productGroupCommandFactory = productGroupCommandFactory;
    }

    @Override
    public void execute(UpdateProductPriceCommand command) {
        ProductGroup productGroup = productGroupReadManager.findById(command.productGroupId());

        if (command.sellerId() != null) {
            validateOwnership(productGroup, command.sellerId());
        }

        Price newPrice = Price.of(command.regularPrice(), command.currentPrice());
        Instant now = productGroupCommandFactory.now();

        ProductGroup updated =
                productGroup.update(
                        productGroup.getCategoryId(),
                        productGroup.getBrandId(),
                        productGroup.getName(),
                        newPrice,
                        productGroup.getShippingPolicyId(),
                        productGroup.getRefundPolicyId(),
                        now);

        productGroupPersistenceManager.persist(updated);
    }

    private void validateOwnership(ProductGroup productGroup, Long sellerId) {
        if (!productGroup.getSellerIdValue().equals(sellerId)) {
            throw new IllegalArgumentException("해당 상품그룹의 수정 권한이 없습니다");
        }
    }
}
