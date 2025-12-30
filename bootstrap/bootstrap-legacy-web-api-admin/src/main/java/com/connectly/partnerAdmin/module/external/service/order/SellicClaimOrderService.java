package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrder;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.ClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.service.OrderUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SellicClaimOrderService extends AbstractClaimOrderExternalService<SellicClaimOrder> {

    public SellicClaimOrderService(ExternalOrderFetchService externalOrderFetchService, OrderUpdateService<ClaimOrder> orderUpdateService) {
        super(externalOrderFetchService, orderUpdateService);
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.SEWON;
    }

    @Override
    public UpdateOrderResponse interlockClaimOrder(SellicClaimOrder sellicClaimOrder) {
        long orderId = fetchOrderId(sellicClaimOrder.getExMallOrderId());

        OrderStatus orderStatus;

        if(sellicClaimOrder.isCancelRequest()) orderStatus = OrderStatus.CANCEL_REQUEST;
        else orderStatus = OrderStatus.RETURN_REQUEST;

        ClaimOrder claimOrder = toClaimOrder(orderId, orderStatus, sellicClaimOrder);

        return updateClaimOrder(claimOrder);
    }



}
