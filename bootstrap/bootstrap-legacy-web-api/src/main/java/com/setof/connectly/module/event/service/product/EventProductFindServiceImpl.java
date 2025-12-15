package com.setof.connectly.module.event.service.product;

import com.setof.connectly.module.event.dto.EventProductStockCheck;
import com.setof.connectly.module.event.enums.EventProductType;
import com.setof.connectly.module.event.repository.EventProductFindRepository;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventProductFindServiceImpl implements EventProductFindService {
    private final EventProductFindRepository eventProductFindRepository;
    private final EventProductRedisQueryService eventProductRedisQueryService;
    private final EventProductRedisFindService eventProductRedisFindService;

    @Override
    public List<EventProductStockCheck> fetchEventProductStockCheck(List<Long> productGroupIds) {
        List<String> values =
                eventProductRedisFindService.fetchEventProductStockCheck(productGroupIds);

        List<String> filteredValues =
                values.stream().filter(Objects::nonNull).collect(Collectors.toList());

        if (!filteredValues.isEmpty()) {
            return parseEventProductStockCheck(values);
        } else {
            List<EventProductStockCheck> eventProductStockCheck =
                    fetchEventProductStockCheckInDb(productGroupIds);
            eventProductStockCheck.forEach(
                    e -> {
                        e.getEventPeriod().checkEventPeriod();
                        eventProductRedisQueryService.saveEventProductStockCheckInCache(e);
                    });

            return eventProductStockCheck;
        }
    }

    @Override
    public EventProductType fetchEventProductType(long productGroupId) {
        String value = eventProductRedisFindService.fetchEventProduct(productGroupId);
        if (StringUtils.hasText(value)) return parseEventProduct(value).getEventProductType();
        else {
            Optional<EventProductStockCheck> eventProductStockCheck =
                    eventProductFindRepository.fetchEventProduct(productGroupId);
            if (eventProductStockCheck.isPresent()) {
                EventProductStockCheck findEventProductStockCheck = eventProductStockCheck.get();
                eventProductRedisQueryService.saveEventProductStockCheckInCache(
                        findEventProductStockCheck);
                return findEventProductStockCheck.getEventProductType();
            }
            return EventProductType.NONE;
        }
    }

    private List<EventProductStockCheck> fetchEventProductStockCheckInDb(
            List<Long> productGroupIds) {
        return eventProductFindRepository.fetchEventProducts(productGroupIds);
    }

    private EventProductStockCheck parseEventProduct(String value) {
        return JsonUtils.fromJson(value, EventProductStockCheck.class);
    }

    private List<EventProductStockCheck> parseEventProductStockCheck(List<String> values) {
        return values.stream()
                .map(s -> JsonUtils.fromJson(s, EventProductStockCheck.class))
                .collect(Collectors.toList());
    }
}
