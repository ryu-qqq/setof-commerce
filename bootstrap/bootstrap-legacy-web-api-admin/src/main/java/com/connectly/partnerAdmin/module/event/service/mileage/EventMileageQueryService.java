package com.connectly.partnerAdmin.module.event.service.mileage;


import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.event.dto.CreateMileageEvent;
import com.connectly.partnerAdmin.module.event.dto.EventMileageDto;
import com.connectly.partnerAdmin.module.event.entity.Event;
import com.connectly.partnerAdmin.module.event.entity.EventMileage;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.connectly.partnerAdmin.module.event.mapper.EventMileageMapper;
import com.connectly.partnerAdmin.module.event.repository.mileage.EventMileageRepository;
import com.connectly.partnerAdmin.module.event.service.AbstractSubEventQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class EventMileageQueryService extends AbstractSubEventQueryService<CreateMileageEvent> {

    private final EventMileageMapper eventMileageMapper;
    private final EventMileageRepository eventMileageRepository;
    private final EventMileageRedisQueryService eventMileageRedisQueryService;

    @Override
    public EventType getEventType() {
        return EventType.MILEAGE;
    }

    @Override
    public void createEvents(Event event, List<CreateMileageEvent> subEvents) {
        Set<EventMileage> eventMileages = subEvents.stream()
                .map(createMileageEvent ->
                        eventMileageMapper.toEventMileage(event.getId(), createMileageEvent))
                .collect(Collectors.toSet());

        eventMileageRepository.saveAll(eventMileages);
        saveEventMileageInCache(eventMileages, event.getEventDetail().getDisplayPeriod());
    }

    private void saveEventMileageInCache(Set<EventMileage> eventMileages, DisplayPeriod eventPeriod){
        eventMileages.forEach(
                e -> {
                    EventMileageDto eventMileageDto = EventMileageDto.builder()
                            .mileageRate(e.getMileageRate())
                            .expirationDate(e.getExpirationDate())
                            .eventPeriod(eventPeriod)
                            .build();
                    eventMileageRedisQueryService.saveEventMileage(eventMileageDto);
                });
    }

}
