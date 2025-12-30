package com.connectly.partnerAdmin.module.event.mapper;

import com.connectly.partnerAdmin.module.event.dto.CreateMileageEvent;
import com.connectly.partnerAdmin.module.event.entity.EventMileage;

public interface EventMileageMapper {
    EventMileage toEventMileage(long eventId, CreateMileageEvent createMileageEvent);
}
