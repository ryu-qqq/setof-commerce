package com.connectly.partnerAdmin.module.event.service;

import com.connectly.partnerAdmin.module.event.dto.SubEvent;
import com.connectly.partnerAdmin.module.event.entity.Event;
import com.connectly.partnerAdmin.module.event.enums.EventType;

import java.util.List;

public interface SubEventQueryService <T extends SubEvent> {

    EventType getEventType();
    void createEvents(Event event, List<T> subEvents);

}
