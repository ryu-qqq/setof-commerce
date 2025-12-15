package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.ComponentFactoryMakeDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ComponentFactoryStrategy
        extends AbstractProvider<
                ComponentType,
                ComponentFactory<? extends SubComponent, ? extends ComponentFactoryMakeDto>> {

    public ComponentFactoryStrategy(
            List<ComponentFactory<? extends SubComponent, ? extends ComponentFactoryMakeDto>>
                    factories) {
        for (ComponentFactory<? extends SubComponent, ? extends ComponentFactoryMakeDto> factory :
                factories) {
            map.put(factory.getComponentType(), factory);
        }
    }
}
