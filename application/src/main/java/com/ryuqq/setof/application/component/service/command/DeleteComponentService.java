package com.ryuqq.setof.application.component.service.command;

import com.ryuqq.setof.application.component.dto.command.DeleteComponentCommand;
import com.ryuqq.setof.application.component.manager.command.ComponentPersistenceManager;
import com.ryuqq.setof.application.component.manager.query.ComponentReadManager;
import com.ryuqq.setof.application.component.port.in.command.DeleteComponentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import org.springframework.stereotype.Service;

/**
 * Component 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteComponentService implements DeleteComponentUseCase {

    private final ComponentReadManager componentReadManager;
    private final ComponentPersistenceManager componentPersistenceManager;

    public DeleteComponentService(
            ComponentReadManager componentReadManager,
            ComponentPersistenceManager componentPersistenceManager) {
        this.componentReadManager = componentReadManager;
        this.componentPersistenceManager = componentPersistenceManager;
    }

    @Override
    public void execute(DeleteComponentCommand command) {
        Component component = componentReadManager.findById(command.componentId());
        component.delete();
        componentPersistenceManager.persist(component);
    }
}
