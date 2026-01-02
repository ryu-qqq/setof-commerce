package com.connectly.partnerAdmin.module.event.mapper;


import com.connectly.partnerAdmin.module.event.dto.CreateMileageEvent;
import com.connectly.partnerAdmin.module.event.entity.EventMileage;
import org.springframework.stereotype.Component;

@Component
public class EventMileageMapperImpl implements EventMileageMapper{

    @Override
    public EventMileage toEventMileage(long eventId, CreateMileageEvent createMileageEvent){
        return EventMileage.builder()
                .eventId(eventId)
                .mileageRate(createMileageEvent.getMileageRate())
                .expirationDate(createMileageEvent.getExpirationDate())
                .mileageType(createMileageEvent.getMileageType())
                .mileageAmount(createMileageEvent.getMileageAmount())
                .build();
    }

}
