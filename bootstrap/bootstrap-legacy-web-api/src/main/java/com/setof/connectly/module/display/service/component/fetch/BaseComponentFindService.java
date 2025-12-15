package com.setof.connectly.module.display.service.component.fetch;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.ComponentFactoryMakeDto;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.factory.ComponentFactory;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.service.component.SubComponentFindService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class BaseComponentFindService implements SubComponentFindService {

    private final ComponentFactoryStrategy componentFactoryStrategy;

    protected Map<Long, Integer> displayOrderMap(List<ComponentQueryDto> componentQueries) {
        return componentQueries.stream()
                .collect(
                        Collectors.toMap(
                                ComponentQueryDto::getComponentId,
                                ComponentQueryDto::getDisplayOrder,
                                (existing, replacement) -> existing));
    }

    protected Map<Long, ComponentQueryDto> componentQueryMap(
            List<ComponentQueryDto> componentQueries) {
        return componentQueries.stream()
                .collect(Collectors.toMap(ComponentQueryDto::getComponentId, Function.identity()));
    }

    protected <U extends ComponentQuery> Map<Long, List<U>> toListMap(List<U> componentQueries) {
        return componentQueries.stream().collect(Collectors.groupingBy(U::getComponentId));
    }

    protected <U extends ComponentQuery> Map<Long, U> toMap(List<U> componentQueries) {
        return componentQueries.stream()
                .collect(Collectors.toMap(U::getComponentId, Function.identity()));
    }

    protected <U extends SubComponent, K extends ComponentFactoryMakeDto>
            SubComponent toComponentDetail(K makeDto) {
        ComponentFactory<U, K> componentFactory =
                (ComponentFactory<U, K>) componentFactoryStrategy.get(getComponentType());
        return componentFactory.makeSubComponent(makeDto);
    }
}
