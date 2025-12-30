package com.connectly.partnerAdmin.module.event.mapper;


import com.connectly.partnerAdmin.module.event.dto.CreateProductEvent;
import com.connectly.partnerAdmin.module.event.entity.EventProduct;
import org.springframework.stereotype.Component;

@Component
public class EventProductMapperImpl implements EventProductMapper{

    public EventProduct toEventProduct(long eventId, CreateProductEvent createProductEvent){
        return EventProduct.builder()
                .eventId(eventId)
                .productGroupId(createProductEvent.getProductGroupId())
                .limitYn(createProductEvent.getLimitYn())
                .limitQty(createProductEvent.getLimitQty())
                .eventPayType(createProductEvent.getEventPayType())
                .eventProductType(createProductEvent.getEventProductType())
                .rewardsMileage(createProductEvent.getRewardsMileage())
                .build();
    }

}
