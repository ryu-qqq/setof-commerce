package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoClaimOrder;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.ClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.service.OrderUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OcoClaimOrderService extends AbstractClaimOrderExternalService<OcoClaimOrder> {


    public OcoClaimOrderService(ExternalOrderFetchService externalOrderFetchService, OrderUpdateService<ClaimOrder> orderUpdateService) {
        super(externalOrderFetchService, orderUpdateService);
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }


    @Override
    public UpdateOrderResponse interlockClaimOrder(OcoClaimOrder ocoClaimOrder) {
        long orderId = fetchOrderId(ocoClaimOrder.getExMallOrderId());
        OrderStatus orderStatus  = OrderStatus.RETURN_REQUEST;

        ClaimOrder claimOrder = toClaimOrder(orderId, orderStatus, ocoClaimOrder);

        return updateClaimOrder(claimOrder);
    }
}
