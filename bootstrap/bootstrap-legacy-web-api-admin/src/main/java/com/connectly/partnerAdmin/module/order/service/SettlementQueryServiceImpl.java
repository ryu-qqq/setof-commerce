package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.Settlement;
import com.connectly.partnerAdmin.module.order.repository.SettlementRepository;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SettlementQueryServiceImpl implements SettlementQueryService {

    private final SettlementRepository settlementRepository;

    @Override
    public void saveSettlement(Order order, BusinessSellerContext businessSellerContext) {
        Settlement settlement = Settlement.builder()
                .sellerCommissionRate(businessSellerContext.getCommissionRate())
                .directDiscountPrice(Money.ZERO.getAmount())
                .useMileageAmount(Money.ZERO.getAmount())
                .directDiscountSellerBurdenRatio(0)
                .mileageSellerBurdenRatio(0)
                .order(order)
                .build();

        settlementRepository.save(settlement);
    }

}
