package com.ryuqq.setof.application.componentitem.port.out.command;

import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import java.util.List;

/**
 * ComponentItem 영속성 Outbound Port (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * <p>삭제는 Soft Delete (도메인에서 상태 변경 후 persist) 권장
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ComponentItemPersistencePort {

    /**
     * ComponentItem 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param componentItem 저장/수정할 ComponentItem
     * @return 저장된 ComponentItem ID
     */
    ComponentItemId persist(ComponentItem componentItem);

    /**
     * ComponentItem 목록 일괄 저장
     *
     * @param componentItems 저장할 ComponentItem 목록
     * @return 저장된 ComponentItem ID 목록
     */
    List<ComponentItemId> persistAll(List<ComponentItem> componentItems);

    /**
     * Component ID로 모든 아이템 삭제 (Soft Delete)
     *
     * @param componentId Component ID
     */
    void deleteAllByComponentId(ComponentId componentId);
}
