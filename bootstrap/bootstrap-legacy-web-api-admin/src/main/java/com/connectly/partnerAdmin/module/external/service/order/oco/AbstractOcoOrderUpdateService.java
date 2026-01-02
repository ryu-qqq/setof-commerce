package com.connectly.partnerAdmin.module.external.service.order.oco;

import com.connectly.partnerAdmin.module.external.client.OcoClient;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderItem;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderWrapper;
import com.connectly.partnerAdmin.module.external.dto.order.oco.query.OcoOrderUpdate;
import com.connectly.partnerAdmin.module.external.exception.ExMallOrderCanceledException;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallOrderNotFoundException;
import com.connectly.partnerAdmin.module.external.payload.OcoResponse;
import com.connectly.partnerAdmin.module.external.handler.OcoResponseHandler;
import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.order.dto.OrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public abstract class AbstractOcoOrderUpdateService<T extends UpdateOrder> implements OcoOrderUpdateService<T> {

    private final OcoClient ocoClient;
    private final OcoResponseHandler<OcoOrderWrapper> ocoResponseHandler;
    private final OrderFetchService orderFetchService;


    protected OcoOrder fetchOcoOrder(long externalIdx){
        ResponseEntity<OcoResponse<OcoOrderWrapper>> response = ocoClient.fetchOrder(externalIdx);
        OcoOrderWrapper ocoOrderWrapper = ocoResponseHandler.handleResponse(response);
        return ocoOrderWrapper.getOrderInfo();
    }

    protected void updateOcoOrder(OcoOrderUpdate ocoOrderUpdate){
        ResponseEntity<OcoResponse<OcoOrderWrapper>> response = ocoClient.updateOrder(ocoOrderUpdate);
        ocoResponseHandler.handleResponse(response);
    }


    protected void cancelOcoOrder(OcoOrderUpdate ocoOrderUpdate){
        ResponseEntity<OcoResponse<OcoOrderWrapper>> response = ocoClient.cancelOrder(ocoOrderUpdate);
        ocoResponseHandler.handleResponse(response);
    }


    protected String fetchExternalProductGroups(long orderId){
        OrderResponse orderResponse = orderFetchService.fetchOrder(orderId);
        OrderProduct orderProduct = orderResponse.getOrderProduct();
        return orderProduct.getOption().replaceAll("/", "");
    }



    protected long getOtid(OcoOrder ocoOrder, ExMallOrderUpdate<T> exMallOrderUpdate) throws ExMallOrderCanceledException {
        String optionName = fetchExternalProductGroups(exMallOrderUpdate.getOrderId());

        Optional<OcoOrderItem> ocoOrderItemOpt = ocoOrder.getOrderItemList().stream()
                .filter(ocoOrderItem -> ocoOrderItem.getOptionItem().replaceAll("/", "").equals(optionName.replaceAll(" ", "")))
                .findFirst();

        if(ocoOrderItemOpt.isPresent()){
            OcoOrderItem ocoOrderItem = ocoOrderItemOpt.get();
            if(ocoOrderItem.isCanceledOrder()) throw new ExMallOrderCanceledException(SiteName.OCO, exMallOrderUpdate.getExMallOrderId());
            else return ocoOrderItem.getOtid();
        }

        throw new ExternalMallOrderNotFoundException(SiteName.OCO.getName(), exMallOrderUpdate.getExMallOrderId());
    }





}
