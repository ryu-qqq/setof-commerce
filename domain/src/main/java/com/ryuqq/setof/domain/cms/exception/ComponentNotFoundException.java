package com.ryuqq.setof.domain.cms.exception;

import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * ComponentNotFoundException - Component를 찾을 수 없을 때 발생
 *
 * <p>HTTP 응답: 404 NOT FOUND
 *
 * @author development-team
 * @since 1.0.0
 */
public class ComponentNotFoundException extends DomainException {

    /**
     * ID로 Component를 찾을 수 없을 때 생성
     *
     * @param componentId Component ID
     */
    public ComponentNotFoundException(ComponentId componentId) {
        super(
                CmsErrorCode.COMPONENT_NOT_FOUND,
                String.format("Component not found: %d", componentId.value()),
                Map.of("componentId", componentId.value()));
    }
}
