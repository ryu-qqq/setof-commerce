package com.ryuqq.setof.application.componentitem.service.command;

import com.ryuqq.setof.application.componentitem.dto.command.CreateComponentItemCommand;
import com.ryuqq.setof.application.componentitem.factory.command.ComponentItemCommandFactory;
import com.ryuqq.setof.application.componentitem.manager.command.ComponentItemPersistenceManager;
import com.ryuqq.setof.application.componentitem.port.in.command.CreateComponentItemUseCase;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateComponentItemService - ComponentItem 생성 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional
public class CreateComponentItemService implements CreateComponentItemUseCase {

    private final ComponentItemCommandFactory commandFactory;
    private final ComponentItemPersistenceManager persistenceManager;

    public CreateComponentItemService(
            ComponentItemCommandFactory commandFactory,
            ComponentItemPersistenceManager persistenceManager) {
        this.commandFactory = commandFactory;
        this.persistenceManager = persistenceManager;
    }

    @Override
    public Long create(CreateComponentItemCommand command) {
        ComponentItem componentItem = commandFactory.toDomain(command);
        ComponentItemId componentItemId = persistenceManager.persist(componentItem);
        return componentItemId.value();
    }

    @Override
    public List<Long> createAll(List<CreateComponentItemCommand> commands) {
        List<ComponentItem> componentItems = commandFactory.toDomainList(commands);
        List<ComponentItemId> componentItemIds = persistenceManager.persistAll(componentItems);
        return componentItemIds.stream().map(ComponentItemId::value).toList();
    }
}
