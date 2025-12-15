package com.setof.connectly.module.event.service.mileage;

import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;
import com.setof.connectly.module.event.repository.EventMileageFindRepository;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventMileageFindServiceImpl implements EventMileageFindService {

    private final EventMileageRedisFindService eventMileageRedisFindService;

    private final EventMileageRedisQueryService eventMileageRedisQueryService;
    private final EventMileageFindRepository eventMileageFindRepository;

    @Override
    public EventMileageDto fetchEventMileage(EventMileageType eventMileageType) {
        String value = eventMileageRedisFindService.fetchEventMileage(eventMileageType);

        if (StringUtils.hasText(value)) return parseEventMileageDto(value);

        return fetchEventMileageInDb(eventMileageType);
    }

    private EventMileageDto fetchEventMileageInDb(EventMileageType eventMileageType) {
        Optional<EventMileageDto> eventMileageDto =
                eventMileageFindRepository.fetchEventMileageInfo(eventMileageType);
        if (eventMileageDto.isPresent()) {
            eventMileageRedisQueryService.saveEventMileage(eventMileageDto.get());
            return eventMileageDto.get();
        }

        EventMileageDto defaultEventMileage = EventMileageDto.defaultEventMileageSetting();
        eventMileageRedisQueryService.saveEventMileage(defaultEventMileage);
        return defaultEventMileage;
    }

    private EventMileageDto parseEventMileageDto(String value) {
        return JsonUtils.fromJson(value, EventMileageDto.class);
    }
}
