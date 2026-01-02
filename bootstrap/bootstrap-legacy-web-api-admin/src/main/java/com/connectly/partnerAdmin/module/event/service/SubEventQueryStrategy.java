package com.connectly.partnerAdmin.module.event.service;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.event.dto.SubEvent;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubEventQueryStrategy extends AbstractProvider<EventType, SubEventQueryService<? extends SubEvent>> {

    public SubEventQueryStrategy(List<SubEventQueryService<? extends SubEvent>> services) {
        for (SubEventQueryService<? extends SubEvent> service : services) {
            map.put(service.getEventType(), service);
        }
    }
}
