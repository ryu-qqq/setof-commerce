package com.ryuqq.setof.application.productgroup.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ProductGroupBundleFactory - 상품그룹 번들을 생성하는 Factory.
 *
 * <p>커맨드로부터 도메인 객체와 번들을 생성합니다.
 */
@Component
public class ProductGroupBundleFactory {

    private final TimeProvider timeProvider;
    private final ProductGroupReadManager productGroupReadManager;

    public ProductGroupBundleFactory(
            TimeProvider timeProvider, ProductGroupReadManager productGroupReadManager) {
        this.timeProvider = timeProvider;
        this.productGroupReadManager = productGroupReadManager;
    }

    /**
     * 등록 커맨드로부터 ProductGroupRegistrationBundle을 생성합니다.
     *
     * @param command 등록 커맨드
     * @return ProductGroupRegistrationBundle 인스턴스
     */
    public ProductGroupRegistrationBundle createRegistrationBundle(
            RegisterProductGroupCommand command) {
        Instant now = timeProvider.now();

        ProductGroup productGroup =
                ProductGroup.forNew(
                        command.productGroupId(),
                        SellerId.of(command.sellerId()),
                        BrandId.of(command.brandId()),
                        CategoryId.of(command.categoryId()),
                        ShippingPolicyId.of(command.shippingPolicyId()),
                        RefundPolicyId.of(command.refundPolicyId()),
                        ProductGroupName.of(command.productGroupName()),
                        OptionType.valueOf(command.optionType()),
                        Money.of(command.regularPrice()),
                        Money.of(command.currentPrice()),
                        Money.of(command.currentPrice()),
                        now);

        return new ProductGroupRegistrationBundle(productGroup, command, now);
    }

    /**
     * 수정 커맨드로부터 ProductGroupUpdateBundle을 생성합니다.
     *
     * <p>기존 상품그룹을 조회한 뒤 번들로 묶어 반환합니다.
     *
     * @param command 수정 커맨드
     * @return ProductGroupUpdateBundle 인스턴스
     */
    public ProductGroupUpdateBundle createUpdateBundle(UpdateProductGroupFullCommand command) {
        Instant now = timeProvider.now();
        ProductGroupId productGroupId = ProductGroupId.of(command.productGroupId());
        ProductGroup productGroup = productGroupReadManager.getById(productGroupId);
        return new ProductGroupUpdateBundle(productGroup, command, now);
    }
}
