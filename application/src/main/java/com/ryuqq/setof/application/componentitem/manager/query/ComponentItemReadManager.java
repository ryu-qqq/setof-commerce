package com.ryuqq.setof.application.componentitem.manager.query;

import com.ryuqq.setof.application.componentitem.port.out.query.ComponentItemQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ComponentItemReadManager - ComponentItem 조회 Manager
 *
 * <p>QueryPort를 통한 조회 로직을 캡슐화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ComponentItemReadManager {

    private final ComponentItemQueryPort queryPort;

    public ComponentItemReadManager(ComponentItemQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * ComponentItem ID로 조회
     *
     * @param componentItemId ComponentItem ID
     * @return ComponentItem Optional
     */
    public Optional<ComponentItem> findById(ComponentItemId componentItemId) {
        return queryPort.findById(componentItemId);
    }

    /**
     * Component ID로 활성 아이템 목록 조회
     *
     * @param componentId Component ID
     * @return 활성 ComponentItem 목록
     */
    public List<ComponentItem> findActiveByComponentId(ComponentId componentId) {
        return queryPort.findActiveByComponentId(componentId);
    }

    /**
     * Component ID로 전체 아이템 목록 조회
     *
     * @param componentId Component ID
     * @return ComponentItem 목록
     */
    public List<ComponentItem> findAllByComponentId(ComponentId componentId) {
        return queryPort.findAllByComponentId(componentId);
    }

    /**
     * 여러 Component ID로 활성 아이템 목록 조회
     *
     * @param componentIds Component ID 목록
     * @return 활성 ComponentItem 목록
     */
    public List<ComponentItem> findActiveByComponentIds(List<ComponentId> componentIds) {
        return queryPort.findActiveByComponentIds(componentIds);
    }

    /**
     * Reference ID와 타입으로 아이템 조회
     *
     * @param referenceId 참조 ID
     * @param itemType 아이템 타입
     * @return ComponentItem 목록
     */
    public List<ComponentItem> findByReferenceIdAndType(
            Long referenceId, ComponentItemType itemType) {
        return queryPort.findByReferenceIdAndType(referenceId, itemType);
    }
}
