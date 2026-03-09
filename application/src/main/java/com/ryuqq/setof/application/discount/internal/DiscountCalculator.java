package com.ryuqq.setof.application.discount.internal;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.AppliedDiscount;
import com.ryuqq.setof.domain.discount.vo.DiscountedPrice;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * DiscountCalculator - 할인 계산 서비스.
 *
 * <p>여러 할인 정책의 스태킹 규칙을 적용하여 최종 가격을 계산합니다.
 *
 * <p>적용 규칙:
 *
 * <ol>
 *   <li>StackingGroup별로 그루핑
 *   <li>각 그룹 내에서 Priority 최상위 1개 선택 (배타적)
 *   <li>그룹 간 applicationOrder 순서로 순차 적용 (복합)
 *   <li>총 할인액이 currentPrice를 초과하지 않도록 보장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountCalculator {

    /**
     * 상품에 적용 가능한 할인 정책들을 스태킹 규칙에 따라 적용.
     *
     * @param regularPrice 정가 (할인율 계산 기준)
     * @param currentPrice 현재가 (할인 적용 기준)
     * @param applicablePolicies 적용 가능한 할인 정책 목록
     * @return 할인 계산 결과
     */
    public DiscountedPrice calculate(
            Money regularPrice, Money currentPrice, List<DiscountPolicy> applicablePolicies) {

        if (applicablePolicies == null || applicablePolicies.isEmpty()) {
            return DiscountedPrice.noDiscount(currentPrice);
        }

        Map<StackingGroup, List<DiscountPolicy>> groupedPolicies =
                applicablePolicies.stream()
                        .collect(Collectors.groupingBy(DiscountPolicy::stackingGroup));

        List<AppliedDiscount> appliedDiscounts = new ArrayList<>();
        Money runningPrice = currentPrice;
        Money totalDiscount = Money.zero();

        List<StackingGroup> sortedGroups =
                groupedPolicies.keySet().stream()
                        .sorted(Comparator.comparingInt(StackingGroup::applicationOrder))
                        .toList();

        for (StackingGroup group : sortedGroups) {
            List<DiscountPolicy> groupPolicies = groupedPolicies.get(group);

            DiscountPolicy selected = selectHighestPriority(groupPolicies);
            if (selected == null) {
                continue;
            }

            Money discountAmount = selected.calculateDiscountAmount(runningPrice);
            if (discountAmount.isZero()) {
                continue;
            }

            if (discountAmount.isGreaterThan(runningPrice)) {
                discountAmount = runningPrice;
            }

            runningPrice = Money.of(runningPrice.value() - discountAmount.value());
            totalDiscount = totalDiscount.add(discountAmount);

            appliedDiscounts.add(AppliedDiscount.of(selected.id(), group, discountAmount, 0.0));
        }

        List<AppliedDiscount> withRatios =
                AppliedDiscount.withShareRatios(appliedDiscounts, totalDiscount);

        int totalDiscountRate = Money.discountRate(regularPrice, runningPrice);

        return DiscountedPrice.of(runningPrice, totalDiscountRate, withRatios);
    }

    private DiscountPolicy selectHighestPriority(List<DiscountPolicy> policies) {
        return policies.stream()
                .max(Comparator.comparingInt(DiscountPolicy::priorityValue))
                .orElse(null);
    }
}
