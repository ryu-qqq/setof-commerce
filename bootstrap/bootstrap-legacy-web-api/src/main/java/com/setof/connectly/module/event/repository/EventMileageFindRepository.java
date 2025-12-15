package com.setof.connectly.module.event.repository;

import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;
import java.util.Optional;

public interface EventMileageFindRepository {
    Optional<EventMileageDto> fetchEventMileageInfo(EventMileageType eventMileageType);
}
