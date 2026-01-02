package com.connectly.partnerAdmin.module.event.service;


import com.connectly.partnerAdmin.module.event.dto.CreateEvent;
import com.connectly.partnerAdmin.module.event.dto.SubEvent;
import com.connectly.partnerAdmin.module.event.entity.Event;
import com.connectly.partnerAdmin.module.event.entity.embedded.EventDetail;
import com.connectly.partnerAdmin.module.event.mapper.EventMapper;
import com.connectly.partnerAdmin.module.event.repository.product.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class EventQueryServiceImpl implements EventQueryService{

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final SubEventQueryStrategy subEventQueryStrategy;

    @Override
    public EventDetail enrollEvent(CreateEvent createEvent){
        Event event = eventMapper.toEvent(createEvent);
        Event savedEvent = saveEvent(event);
        SubEventQueryService<SubEvent> subEventQueryService = (SubEventQueryService<SubEvent>) subEventQueryStrategy.get(createEvent.getEventType());
        subEventQueryService.createEvents(savedEvent, createEvent.getSubEvents());
        return savedEvent.getEventDetail();
    }

    private Event saveEvent(Event event){
        return eventRepository.save(event);
    }


}
