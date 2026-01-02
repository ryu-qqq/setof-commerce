package com.connectly.partnerAdmin.module.event.mapper;


import com.connectly.partnerAdmin.module.event.dto.CreateEvent;
import com.connectly.partnerAdmin.module.event.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapperImpl implements EventMapper{

    @Override
    public Event toEvent(CreateEvent createEvent){
        return Event.builder()
                .eventType(createEvent.getEventType())
                .eventDetail(createEvent.getEventDetail())
                .build();
    }


}
