package com.ryuqq.setof.application.componentitem.port.in.query;

import com.ryuqq.setof.application.componentitem.dto.response.ComponentItemResponse;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import java.util.List;

/**
 * ComponentItem 목록 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetComponentItemsUseCase {

    /**
     * Component ID로 활성 아이템 목록 조회
     *
     * @param componentId Component ID
     * @return ComponentItem 응답 목록
     */
    List<ComponentItemResponse> getActiveByComponentId(ComponentId componentId);

    /**
     * 여러 Component ID로 활성 아이템 목록 조회
     *
     * @param componentIds Component ID 목록
     * @return ComponentItem 응답 목록
     */
    List<ComponentItemResponse> getActiveByComponentIds(List<ComponentId> componentIds);
}
