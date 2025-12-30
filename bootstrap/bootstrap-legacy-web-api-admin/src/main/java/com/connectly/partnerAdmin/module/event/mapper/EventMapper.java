package com.connectly.partnerAdmin.module.event.mapper;

import com.connectly.partnerAdmin.module.event.dto.CreateEvent;
import com.connectly.partnerAdmin.module.event.entity.Event;


public interface EventMapper {

    Event toEvent(CreateEvent createEvent);
}
