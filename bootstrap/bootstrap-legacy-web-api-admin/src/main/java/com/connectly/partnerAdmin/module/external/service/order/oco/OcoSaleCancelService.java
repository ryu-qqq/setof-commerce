package com.connectly.partnerAdmin.module.external.service.order.oco;

import com.connectly.partnerAdmin.module.external.annotation.RequiresAccessToken;
import com.connectly.partnerAdmin.module.external.client.OcoClient;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderWrapper;
import com.connectly.partnerAdmin.module.external.dto.order.oco.query.OcoCancelSaleOrder;
import com.connectly.partnerAdmin.module.external.handler.OcoResponseHandler;
import com.connectly.partnerAdmin.module.order.dto.query.ClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OcoSaleCancelService extends AbstractOcoOrderUpdateService<ClaimOrder> {

    private static final String CANCEL_TYPE = "S";

    public OcoSaleCancelService(OcoClient ocoClient, OcoResponseHandler<OcoOrderWrapper> ocoResponseHandler, OrderFetchService orderFetchService) {
        super(ocoClient, ocoResponseHandler, orderFetchService);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.SALE_CANCELLED;
    }

    @RequiresAccessToken(siteName = SiteName.OCO)
    @Override
    public void updateOrder(ExMallOrderUpdate<ClaimOrder> exMallOrderUpdate) {
        OcoOrder ocoOrder = fetchOcoOrder(exMallOrderUpdate.getExMallOrderId());
        long otid = getOtid(ocoOrder, exMallOrderUpdate);


//        OcoNormalOrder ocoNormalOrder = new OcoNormalOrder(otid, OcoOrderStatus.W.name());
//        updateOcoOrder(ocoNormalOrder);

        OcoCancelSaleOrder ocoCancelSaleOrder = new OcoCancelSaleOrder(otid, exMallOrderUpdate.getExMallOrderId(), CANCEL_TYPE);
        cancelOcoOrder(ocoCancelSaleOrder);
    }

}
