package com.connectly.partnerAdmin.module.external.service;

import com.connectly.partnerAdmin.module.external.dto.order.buyma.BuymaOrder;
import com.connectly.partnerAdmin.module.external.dto.product.buyma.BuymaProductDto;
import com.connectly.partnerAdmin.module.external.dto.product.buyma.BuymaProductFailDto;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderIssueService;
import com.connectly.partnerAdmin.module.external.service.product.ExternalProductFetchService;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class BuyMaWebHookService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BUYMA_EVENT_HEADER = "X-Buyma-Event";
    private final ExternalProductFetchService externalProductFetchService;
    private final ExternalOrderIssueService externalOrderIssueService;

    public void sync(HttpServletRequest request, Object object) {
        String webhookHeader = request.getHeader(BUYMA_EVENT_HEADER);
        if (webhookHeader.startsWith("order/")) {
            syncOrder(object);
        }

        if (webhookHeader.startsWith("product/")) {
            syncProduct(webhookHeader, object);
        }


    }

    private void syncProduct(String webhookHeader, Object object){
        if(webhookHeader.equals("product/fail_to_create")){
            BuymaProductFailDto buymaProductFailDto = convertToFailProduct(object);
            Optional<ExternalProduct> externalProductOpt = externalProductFetchService.fetchHasExternalProductEntity(buymaProductFailDto.getProductGroupId(), SiteName.BUYMA.getSiteId());
            externalProductOpt.ifPresentOrElse(ExternalProduct::deActive,
                    () -> {
                        throw new ExternalMallProductNotFoundException(SiteName.BUYMA.getName(), String.valueOf(buymaProductFailDto.getId()));
                    });
        }else{
            BuymaProductDto buymaProduct = convertToProduct(object);
            Optional<ExternalProduct> externalProductOpt = externalProductFetchService.fetchHasExternalProductEntity(buymaProduct.getProductGroupId(), SiteName.BUYMA.getSiteId());
            externalProductOpt.ifPresentOrElse(externalProduct -> {
                        externalProduct.active(String.valueOf(buymaProduct.getId()));
                    },
                    () -> {
                        throw new ExternalMallProductNotFoundException(SiteName.BUYMA.getName(), String.valueOf(buymaProduct.getId()));
                    });
        }
    }

    private void syncOrder(Object object){
        BuymaOrder order = convertToOrder(object);
        externalOrderIssueService.syncOrders(order);
    }

    private BuymaProductFailDto convertToFailProduct(Object object) {
        if (object instanceof LinkedHashMap) {
            return objectMapper.convertValue(object, BuymaProductFailDto.class);
        } else {
            return (BuymaProductFailDto) object;
        }

    }

    private BuymaProductDto convertToProduct(Object object) {
        if (object instanceof LinkedHashMap) {
            return objectMapper.convertValue(object, BuymaProductDto.class);
        } else {
            return (BuymaProductDto) object;
        }
    }

    private BuymaOrder convertToOrder(Object object) {
        if (object instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) object;
            map.put("type", "buymaOrder");
            return objectMapper.convertValue(map, BuymaOrder.class);
        } else {
            return (BuymaOrder) object;
        }
    }


}
