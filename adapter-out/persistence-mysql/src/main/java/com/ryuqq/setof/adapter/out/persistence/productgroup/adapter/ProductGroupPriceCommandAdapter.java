package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupAppliedDiscountJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupAppliedDiscountJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupPriceQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.command.ProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.AppliedDiscount;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupPriceCommandAdapter - 새 스키마(setof) 상품그룹 가격 갱신 Adapter.
 *
 * <p>product_groups 테이블의 할인 관련 필드를 갱신하고, product_group_applied_discounts 테이블에 적용 할인 내역을 저장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceCommandAdapter implements ProductGroupPriceCommandPort {

    private final ProductGroupPriceQueryDslRepository priceRepository;
    private final ProductGroupAppliedDiscountJpaRepository appliedDiscountRepository;

    public ProductGroupPriceCommandAdapter(
            ProductGroupPriceQueryDslRepository priceRepository,
            ProductGroupAppliedDiscountJpaRepository appliedDiscountRepository) {
        this.priceRepository = priceRepository;
        this.appliedDiscountRepository = appliedDiscountRepository;
    }

    @Override
    public void persistAll(List<ProductGroupPriceUpdateData> updates) {
        for (ProductGroupPriceUpdateData update : updates) {
            priceRepository.updatePrice(
                    update.productGroupId(),
                    update.salePrice(),
                    update.discountRate(),
                    update.directDiscountRate(),
                    update.directDiscountPrice());
        }
    }

    @Override
    public void replaceAppliedDiscounts(
            long productGroupId, List<AppliedDiscount> appliedDiscounts, Instant appliedAt) {
        appliedDiscountRepository.deleteByProductGroupId(productGroupId);

        Instant now = Instant.now();
        List<ProductGroupAppliedDiscountJpaEntity> entities =
                appliedDiscounts.stream()
                        .map(
                                ad ->
                                        ProductGroupAppliedDiscountJpaEntity.create(
                                                productGroupId,
                                                ad.discountPolicyId().value(),
                                                ad.stackingGroup().name(),
                                                (int) (ad.shareRatio() * 100),
                                                ad.amount().value(),
                                                BigDecimal.valueOf(ad.shareRatio()),
                                                appliedAt,
                                                now,
                                                now))
                        .toList();

        appliedDiscountRepository.saveAll(entities);
    }
}
