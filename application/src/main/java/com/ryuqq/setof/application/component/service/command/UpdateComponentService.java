package com.ryuqq.setof.application.component.service.command;

import com.ryuqq.setof.application.component.dto.command.UpdateComponentCommand;
import com.ryuqq.setof.application.component.factory.command.ComponentCommandFactory;
import com.ryuqq.setof.application.component.manager.command.ComponentPersistenceManager;
import com.ryuqq.setof.application.component.manager.query.ComponentReadManager;
import com.ryuqq.setof.application.component.port.in.command.UpdateComponentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import org.springframework.stereotype.Service;

/**
 * Component 수정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateComponentService implements UpdateComponentUseCase {

    private final ComponentReadManager componentReadManager;
    private final ComponentCommandFactory componentCommandFactory;
    private final ComponentPersistenceManager componentPersistenceManager;

    public UpdateComponentService(
            ComponentReadManager componentReadManager,
            ComponentCommandFactory componentCommandFactory,
            ComponentPersistenceManager componentPersistenceManager) {
        this.componentReadManager = componentReadManager;
        this.componentCommandFactory = componentCommandFactory;
        this.componentPersistenceManager = componentPersistenceManager;
    }

    @Override
    public void execute(UpdateComponentCommand command) {
        Component component = componentReadManager.findById(command.componentId());
        componentCommandFactory.applyUpdateComponent(component, command);
        componentPersistenceManager.persist(component);
    }
}
