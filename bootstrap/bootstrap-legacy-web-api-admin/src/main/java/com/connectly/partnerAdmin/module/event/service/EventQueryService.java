package com.connectly.partnerAdmin.module.event.service;

import com.connectly.partnerAdmin.module.event.dto.CreateEvent;
import com.connectly.partnerAdmin.module.event.entity.embedded.EventDetail;

public interface EventQueryService {

    EventDetail enrollEvent(CreateEvent createEvent);
}
