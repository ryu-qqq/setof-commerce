package com.ryuqq.setof.application.component.service.command;

import com.ryuqq.setof.application.component.dto.command.CreateComponentCommand;
import com.ryuqq.setof.application.component.factory.command.ComponentCommandFactory;
import com.ryuqq.setof.application.component.manager.command.ComponentPersistenceManager;
import com.ryuqq.setof.application.component.port.in.command.CreateComponentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import org.springframework.stereotype.Service;

/**
 * Component 생성 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateComponentService implements CreateComponentUseCase {

    private final ComponentCommandFactory componentCommandFactory;
    private final ComponentPersistenceManager componentPersistenceManager;

    public CreateComponentService(
            ComponentCommandFactory componentCommandFactory,
            ComponentPersistenceManager componentPersistenceManager) {
        this.componentCommandFactory = componentCommandFactory;
        this.componentPersistenceManager = componentPersistenceManager;
    }

    @Override
    public Long execute(CreateComponentCommand command) {
        Component component = componentCommandFactory.createComponent(command);
        return componentPersistenceManager.persist(component).value();
    }
}
