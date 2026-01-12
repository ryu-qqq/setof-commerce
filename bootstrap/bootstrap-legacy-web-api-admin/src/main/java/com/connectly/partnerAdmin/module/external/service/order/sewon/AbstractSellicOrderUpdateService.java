package com.connectly.partnerAdmin.module.external.service.order.sewon;

import com.connectly.partnerAdmin.module.external.client.SeWonClient;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrderRequest;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrderResponse;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentRequestDto;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentResponseDto;
import com.connectly.partnerAdmin.module.external.exception.ExMallOrderCanceledException;
import com.connectly.partnerAdmin.module.external.payload.SeWonResponse;
import com.connectly.partnerAdmin.module.external.handler.SewonResponseHandler;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class AbstractSellicOrderUpdateService<T extends UpdateOrder>  implements SellicOrderUpdateService<T> {

    @Value(value = "${sewon.customer_id}")
    private String customerId;
    @Value(value = "${sewon.api_key}")
    private String apiKey;
    private final SeWonClient seWonClient;


    private final SewonResponseHandler<List<SellicClaimOrderResponse>> sewonResponseHandler;
    private final SewonResponseHandler<List<SellicShipmentResponseDto>> sewonShipmentResponseHandler;

    protected void updateSellicOrderShipment(SellicShipmentRequestDto invoice){
        invoice.setApiKey(apiKey);
        invoice.setCustomerId(customerId);

        ResponseEntity<SeWonResponse<List<SellicShipmentResponseDto>>> shipment = seWonClient.createShipment(invoice);
        sewonShipmentResponseHandler.handleResponse(shipment);
    }

    protected void checkSellicClaimOrder(long externalOrderId){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(1);
        LocalDateTime endDate = now.plusDays(1);

        SellicClaimOrderRequest sellicClaimOrderRequest = new SellicClaimOrderRequest(customerId, apiKey, startDate, endDate, externalOrderId);

        ResponseEntity<SeWonResponse<List<SellicClaimOrderResponse>>> response = seWonClient.fetchClaimOrders(sellicClaimOrderRequest);
        List<SellicClaimOrderResponse> sellicClaimOrderResponses = sewonResponseHandler.handleResponse(response);

        // datas가 null인 경우 (클레임 데이터 없음) 처리
        if (sellicClaimOrderResponses == null || sellicClaimOrderResponses.isEmpty()) {
            return;
        }

        Optional<SellicClaimOrderResponse> sellicClaimOrderResponseOpt = sellicClaimOrderResponses.stream()
                .filter(sellicClaimOrderResponse -> sellicClaimOrderResponse.getIdx() == externalOrderId)
                .findFirst();

        if(sellicClaimOrderResponseOpt.isPresent()) throw new ExMallOrderCanceledException(SiteName.OCO, externalOrderId);
    }


}
