package com.ryuqq.setof.application.discount.internal;

import com.ryuqq.setof.application.discount.factory.ProductGroupPriceUpdateFactory;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.manager.ProductGroupPriceCommandManager;
import com.ryuqq.setof.application.discount.manager.ProductGroupPriceReadManager;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 할인 가격 재계산 프로세서.
 *
 * <p>타겟에 해당하는 상품들의 가격을 현재 활성 할인 정책으로 재계산하고 일괄 갱신합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPriceRecalculateProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(DiscountPriceRecalculateProcessor.class);

    private final ProductGroupPriceReadManager priceReadManager;
    private final ProductGroupPriceCommandManager priceCommandManager;
    private final DiscountPolicyReadManager policyReadManager;
    private final ProductGroupPriceUpdateFactory priceUpdateFactory;

    public DiscountPriceRecalculateProcessor(
            ProductGroupPriceReadManager priceReadManager,
            ProductGroupPriceCommandManager priceCommandManager,
            DiscountPolicyReadManager policyReadManager,
            ProductGroupPriceUpdateFactory priceUpdateFactory) {
        this.priceReadManager = priceReadManager;
        this.priceCommandManager = priceCommandManager;
        this.policyReadManager = policyReadManager;
        this.priceUpdateFactory = priceUpdateFactory;
    }

    /**
     * 타겟에 해당하는 모든 상품의 가격을 재계산하여 일괄 갱신.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     */
    @Transactional
    public void process(DiscountTargetType targetType, long targetId) {
        List<ProductGroupPriceRow> productGroups =
                priceReadManager.findByTarget(targetType, targetId);

        if (productGroups.isEmpty()) {
            log.info("타겟에 해당하는 상품이 없음: {}:{}", targetType, targetId);
            return;
        }

        Instant now = Instant.now();
        List<DiscountPolicy> applicablePolicies =
                policyReadManager.findApplicablePolicies(targetType, targetId, now);

        List<ProductGroupPriceUpdateData> updates =
                priceUpdateFactory.createAll(productGroups, applicablePolicies);

        priceCommandManager.persistAll(updates);

        log.info(
                "상품 가격 갱신 완료: target={}:{}, count={}, appliedPolicies={}",
                targetType,
                targetId,
                updates.size(),
                applicablePolicies.size());
    }
}
