package com.ryuqq.setof.application.component.port.in.query;

import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import com.ryuqq.setof.domain.cms.vo.ComponentId;

/**
 * Component 단건 조회 UseCase (Query)
 *
 * <p>컴포넌트 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetComponentUseCase {

    /**
     * 컴포넌트 단건 조회
     *
     * @param componentId 컴포넌트 ID
     * @return 컴포넌트 응답
     */
    ComponentResponse execute(ComponentId componentId);
}
