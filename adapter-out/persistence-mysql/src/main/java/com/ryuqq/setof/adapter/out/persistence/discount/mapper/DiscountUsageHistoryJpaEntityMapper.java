package com.ryuqq.setof.adapter.out.persistence.discount.mapper;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountUsageHistoryJpaEntity;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.discount.aggregate.DiscountUsageHistory;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountUsageHistoryId;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * DiscountUsageHistoryJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Plain Java 사용
 *   <li>Domain 객체 직접 사용
 *   <li>VO 변환 로직 포함
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountUsageHistoryJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public DiscountUsageHistoryJpaEntity toEntity(DiscountUsageHistory domain) {
        return DiscountUsageHistoryJpaEntity.of(
                domain.idValue(),
                domain.discountPolicyIdValue(),
                domain.memberId(),
                domain.checkoutIdValue(),
                domain.orderIdValue(),
                domain.appliedAmountValue(),
                domain.originalAmountValue(),
                domain.platformRatio(),
                domain.sellerRatio(),
                domain.platformCost(),
                domain.sellerCost(),
                domain.usedAt(),
                domain.createdAt(),
                domain.createdAt()); // updatedAt은 createdAt과 동일 (불변 객체)
    }

    /** Entity -> Domain 변환 */
    public DiscountUsageHistory toDomain(DiscountUsageHistoryJpaEntity entity) {
        return DiscountUsageHistory.fromPersistence(
                DiscountUsageHistoryId.of(entity.getId()),
                DiscountPolicyId.of(entity.getDiscountPolicyId()),
                entity.getMemberId(),
                CheckoutId.fromString(entity.getCheckoutId()),
                OrderId.fromString(entity.getOrderId()),
                DiscountAmount.of(entity.getAppliedAmount()),
                OrderMoney.of(BigDecimal.valueOf(entity.getOriginalAmount())),
                CostShare.of(entity.getPlatformRatio(), entity.getSellerRatio()),
                entity.getPlatformCost(),
                entity.getSellerCost(),
                entity.getUsedAt(),
                entity.getCreatedAt());
    }
}
