package com.ryuqq.setof.application.componentitem.manager.command;

import com.ryuqq.setof.application.componentitem.port.out.command.ComponentItemPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ComponentItemPersistenceManager - ComponentItem 영속성 Manager
 *
 * <p>PersistencePort를 통한 저장 로직을 캡슐화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ComponentItemPersistenceManager {

    private final ComponentItemPersistencePort persistencePort;

    public ComponentItemPersistenceManager(ComponentItemPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    /**
     * ComponentItem 저장
     *
     * @param componentItem 저장할 ComponentItem
     * @return 저장된 ComponentItem ID
     */
    public ComponentItemId persist(ComponentItem componentItem) {
        return persistencePort.persist(componentItem);
    }

    /**
     * ComponentItem 목록 일괄 저장
     *
     * @param componentItems 저장할 ComponentItem 목록
     * @return 저장된 ComponentItem ID 목록
     */
    public List<ComponentItemId> persistAll(List<ComponentItem> componentItems) {
        return persistencePort.persistAll(componentItems);
    }

    /**
     * Component ID로 모든 아이템 삭제
     *
     * @param componentId Component ID
     */
    public void deleteAllByComponentId(ComponentId componentId) {
        persistencePort.deleteAllByComponentId(componentId);
    }
}
