package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.ClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.OrderUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractClaimOrderExternalService<T extends ExMallClaimOrder> implements ExternalClaimOrderService<T> {

    private final ExternalOrderFetchService externalOrderFetchService;
    private final OrderUpdateService<ClaimOrder> orderUpdateService;

    protected long fetchOrderId(long externalIdx){
        return externalOrderFetchService.fetchOrderId(getSiteName().getSiteId(), externalIdx);
    }

    protected ClaimOrder toClaimOrder(long orderId, OrderStatus orderStatus, T t){
        return new ClaimOrder(orderId, orderStatus, false, t.getClaimReason(), t.getClaimDetailReason());
    }

    protected UpdateOrderResponse updateClaimOrder(ClaimOrder claimOrder){
        return orderUpdateService.updateOrderExternalMall(claimOrder);
    }

}
