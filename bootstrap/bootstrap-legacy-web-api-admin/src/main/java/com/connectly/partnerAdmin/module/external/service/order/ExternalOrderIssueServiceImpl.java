package com.connectly.partnerAdmin.module.external.service.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ExternalOrderIssueServiceImpl implements ExternalOrderIssueService{

    private final ExternalOrderFetchService externalOrderFetchService;
    private final ExternalOrderIssueProvider<? extends ExMallOrder> externalOrderIssueProvider;
    private final ExternalClaimOrderOrderProvider<? extends ExMallClaimOrder> externalClaimOrderOrderProvider;

    @Override
    public <T extends ExMallOrder> List<ExternalOrder> syncOrders(T t) {
        boolean b = externalOrderFetchService.doesHasSyncOrder(t.getSiteId(), t.getExMallOrderId());
        if(!b){
            ExternalOrderService<ExMallOrder> externalOrderService = (ExternalOrderService<ExMallOrder>) externalOrderIssueProvider.get(t.getSiteName());
            return externalOrderService.syncOrder(t);
        }
        return new ArrayList<>();
    }

    @Override
    public void syncOrdersUpdate(List<ExMallOrderUpdate<? extends UpdateOrder>> syncOrders) {
        syncOrders.forEach(exMallOrderUpdate -> {
            SiteName siteName = exMallOrderUpdate.getSiteName();
            if(siteName != SiteName.SHEIN && siteName != SiteName.BUYMA && siteName != SiteName.LF){
                ExternalOrderService<ExMallOrder> exMallOrderExternalOrderService = (ExternalOrderService<ExMallOrder>) externalOrderIssueProvider.get(siteName);
                exMallOrderExternalOrderService.syncOrdersUpdate(exMallOrderUpdate);
            }

        });
    }


    @Override
    public <R extends ExMallClaimOrder> UpdateOrderResponse syncClaimOrder(R r) {
        ExternalClaimOrderService<ExMallClaimOrder> exMallOrderInterlockingOrderService = (ExternalClaimOrderService<ExMallClaimOrder>) externalClaimOrderOrderProvider.get(r.getSiteName());
        return exMallOrderInterlockingOrderService.interlockClaimOrder(r);
    }

}
