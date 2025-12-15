package com.setof.connectly.module.order.service.query.snapshot.group;

import com.setof.connectly.module.order.entity.order.Settlement;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.SettlementRepository;
import com.setof.connectly.module.order.repository.snapshot.query.group.OrderSnapShotProductGroupJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductGroupSnapShotQueryService
        implements OrderSnapShotService<OrderSnapShotProductGroup> {

    private final OrderSnapShotProductGroupJdbcRepository orderSnapShotProductGroupJdbcRepository;
    private final SettlementRepository settlementRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductGroup> snapShots) {
        orderSnapShotProductGroupJdbcRepository.saveAll(snapShots);
        for (OrderSnapShotProductGroup productGroup : snapShots) {
            saveSettlement(productGroup);
        }
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_GROUP;
    }

    private void saveSettlement(OrderSnapShotProductGroup orderSnapShotProductGroup) {

        Settlement settlement =
                Settlement.builder()
                        .orderId(orderSnapShotProductGroup.getOrderId())
                        .sellerCommissionRate(
                                orderSnapShotProductGroup
                                        .getSnapShotProductGroup()
                                        .getCommissionRate())
                        .directDiscountPrice(
                                BigDecimal.valueOf(
                                        orderSnapShotProductGroup
                                                .getSnapShotProductGroup()
                                                .getPrice()
                                                .getDirectDiscountPrice()))
                        .useMileageAmount(BigDecimal.ZERO)
                        .directDiscountSellerBurdenRatio(
                                100
                                        - orderSnapShotProductGroup
                                                .getSnapShotProductGroup()
                                                .getShareRatio())
                        .mileageSellerBurdenRatio(0)
                        .build();

        settlementRepository.save(settlement);
    }
}
