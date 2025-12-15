package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupCommand;
import com.ryuqq.setof.application.product.factory.command.ProductGroupCommandFactory;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupUseCase;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 상품그룹 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductGroupReadManager로 기존 ProductGroup 조회
 *   <li>셀러 소유권 검증
 *   <li>도메인 메서드로 수정
 *   <li>ProductGroupPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateProductGroupService implements UpdateProductGroupUseCase {

    private final ProductGroupReadManager productGroupReadManager;
    private final ProductGroupPersistenceManager productGroupPersistenceManager;
    private final ProductGroupCommandFactory productGroupCommandFactory;

    public UpdateProductGroupService(
            ProductGroupReadManager productGroupReadManager,
            ProductGroupPersistenceManager productGroupPersistenceManager,
            ProductGroupCommandFactory productGroupCommandFactory) {
        this.productGroupReadManager = productGroupReadManager;
        this.productGroupPersistenceManager = productGroupPersistenceManager;
        this.productGroupCommandFactory = productGroupCommandFactory;
    }

    @Override
    public void execute(UpdateProductGroupCommand command) {
        ProductGroup productGroup = productGroupReadManager.findById(command.productGroupId());

        // 셀러 소유권 검증
        validateOwnership(productGroup, command.sellerId());

        Instant now = productGroupCommandFactory.now();
        ProductGroup updated = applyUpdates(productGroup, command, now);

        productGroupPersistenceManager.persist(updated);
    }

    private void validateOwnership(ProductGroup productGroup, Long sellerId) {
        if (!productGroup.getSellerIdValue().equals(sellerId)) {
            throw new IllegalArgumentException("해당 상품그룹의 수정 권한이 없습니다");
        }
    }

    private ProductGroup applyUpdates(
            ProductGroup productGroup, UpdateProductGroupCommand command, Instant now) {

        // CategoryId (기존값 또는 새값)
        CategoryId categoryId =
                command.categoryId() != null
                        ? CategoryId.of(command.categoryId())
                        : productGroup.getCategoryId();

        // BrandId (기존값 또는 새값)
        BrandId brandId =
                command.brandId() != null
                        ? BrandId.of(command.brandId())
                        : productGroup.getBrandId();

        // ProductGroupName (기존값 또는 새값)
        ProductGroupName name =
                command.name() != null
                        ? ProductGroupName.of(command.name())
                        : productGroup.getName();

        // Price (기존값 또는 새값)
        Price price =
                Price.of(
                        command.regularPrice() != null
                                ? command.regularPrice()
                                : productGroup.getRegularPriceValue(),
                        command.currentPrice() != null
                                ? command.currentPrice()
                                : productGroup.getCurrentPriceValue());

        // ShippingPolicyId (기존값 또는 새값)
        ShippingPolicyId shippingPolicyId =
                command.shippingPolicyId() != null
                        ? ShippingPolicyId.of(command.shippingPolicyId())
                        : productGroup.getShippingPolicyId();

        // RefundPolicyId (기존값 또는 새값)
        RefundPolicyId refundPolicyId =
                command.refundPolicyId() != null
                        ? RefundPolicyId.of(command.refundPolicyId())
                        : productGroup.getRefundPolicyId();

        return productGroup.update(
                categoryId, brandId, name, price, shippingPolicyId, refundPolicyId, now);
    }
}
