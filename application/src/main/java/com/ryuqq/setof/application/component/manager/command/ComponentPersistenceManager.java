package com.ryuqq.setof.application.component.manager.command;

import com.ryuqq.setof.application.component.port.out.command.ComponentPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component 영속성 Manager (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@org.springframework.stereotype.Component
@Transactional
public class ComponentPersistenceManager {

    private final ComponentPersistencePort componentPersistencePort;

    public ComponentPersistenceManager(ComponentPersistencePort componentPersistencePort) {
        this.componentPersistencePort = componentPersistencePort;
    }

    /**
     * 컴포넌트 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param component 저장/수정할 컴포넌트
     * @return 저장된 Component ID
     */
    public ComponentId persist(Component component) {
        return componentPersistencePort.persist(component);
    }
}
