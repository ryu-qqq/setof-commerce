package com.connectly.partnerAdmin.module.event.service.product;

import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.event.dto.CreateProductEvent;
import com.connectly.partnerAdmin.module.event.dto.EventProductGroupDto;
import com.connectly.partnerAdmin.module.event.dto.EventProductStockCheck;
import com.connectly.partnerAdmin.module.event.entity.Event;
import com.connectly.partnerAdmin.module.event.entity.EventProduct;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.connectly.partnerAdmin.module.event.mapper.EventProductMapper;
import com.connectly.partnerAdmin.module.event.repository.product.EventProductRepository;
import com.connectly.partnerAdmin.module.event.service.AbstractSubEventQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service

public class EventProductQueryService extends AbstractSubEventQueryService<CreateProductEvent> {

    private final EventProductMapper eventProductMapper;
    private final EventProductRepository eventProductRepository;
    private final EventProductRedisQueryService eventProductRedisQueryService;

    public EventProductQueryService(EventProductMapper eventProductMapper, EventProductRepository eventProductRepository, EventProductRedisQueryService eventProductRedisQueryService) {
        this.eventProductMapper = eventProductMapper;
        this.eventProductRepository = eventProductRepository;
        this.eventProductRedisQueryService = eventProductRedisQueryService;
    }

    @Override
    public EventType getEventType() {
        return EventType.PRODUCT;
    }

    @Override
    public void createEvents(Event event, List<CreateProductEvent> subEvents) {
        Set<EventProduct> eventProducts = subEvents.stream()
                .map(createProductEvent ->
                        eventProductMapper.toEventProduct(event.getId(), createProductEvent))
                .collect(Collectors.toSet());

        eventProductRepository.saveAll(eventProducts);
        saveEventProductInCache(eventProducts, event.getEventDetail().getDisplayPeriod());
    }



    public void saveEventProductInCache(Set<EventProduct> eventProducts, DisplayPeriod eventPeriod){

        Map<Long, EventProduct> eventProductMap = eventProducts.stream()
                .collect(Collectors
                        .toMap(EventProduct::getProductGroupId,
                                Function.identity(),
                                (existing, replacement) -> existing));


        ArrayList<Long> productGroupIds = new ArrayList<>(eventProductMap.keySet());
        List<EventProductGroupDto> eventProductGroups = new ArrayList<>();


        eventProductGroups.forEach(e -> {

            EventProduct eventProduct = eventProductMap.get(e.getProductGroupId());
            if(eventProduct!= null){

                EventProductStockCheck eventProductStockCheck = EventProductStockCheck.builder()
                        .productGroupId(eventProduct.getProductGroupId())
                        .eventProductType(eventProduct.getEventProductType())
                        .eventPeriod(eventPeriod)
                        .eventPayType(eventProduct.getEventPayType())
                        .limitQty(eventProduct.getLimitQty())
                        .limitYn(eventProduct.getLimitYn())
                        .rewardsMileage(eventProduct.getRewardsMileage())
                        .productIds(e.getProductIds())
                        .build();

                eventProductRedisQueryService.saveEventProductStockCheckInCache(eventProductStockCheck);
            }

        });


    }

}
