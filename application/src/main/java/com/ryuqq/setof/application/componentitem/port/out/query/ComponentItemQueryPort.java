package com.ryuqq.setof.application.componentitem.port.out.query;

import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemType;
import java.util.List;
import java.util.Optional;

/**
 * ComponentItem 조회 Outbound Port (Query)
 *
 * <p>컴포넌트 아이템 조회를 위한 Outbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ComponentItemQueryPort {

    /**
     * ComponentItem ID로 조회
     *
     * @param componentItemId ComponentItem ID
     * @return ComponentItem Optional
     */
    Optional<ComponentItem> findById(ComponentItemId componentItemId);

    /**
     * Component ID로 활성 아이템 목록 조회
     *
     * @param componentId Component ID
     * @return 활성 ComponentItem 목록 (displayOrder 순)
     */
    List<ComponentItem> findActiveByComponentId(ComponentId componentId);

    /**
     * Component ID로 전체 아이템 목록 조회 (삭제 제외)
     *
     * @param componentId Component ID
     * @return ComponentItem 목록 (displayOrder 순)
     */
    List<ComponentItem> findAllByComponentId(ComponentId componentId);

    /**
     * 여러 Component ID로 활성 아이템 목록 조회
     *
     * @param componentIds Component ID 목록
     * @return 활성 ComponentItem 목록
     */
    List<ComponentItem> findActiveByComponentIds(List<ComponentId> componentIds);

    /**
     * Reference ID와 타입으로 아이템 조회
     *
     * @param referenceId 참조 ID (상품 그룹 ID 등)
     * @param itemType 아이템 타입
     * @return ComponentItem 목록
     */
    List<ComponentItem> findByReferenceIdAndType(Long referenceId, ComponentItemType itemType);
}
