package com.ryuqq.setof.application.component.port.out.query;

import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import java.util.Optional;

/**
 * Component 조회 Outbound Port (Query)
 *
 * <p>컴포넌트 조회를 위한 Outbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ComponentQueryPort {

    /**
     * 컴포넌트 ID로 조회
     *
     * @param componentId 컴포넌트 ID
     * @return 컴포넌트 Optional
     */
    Optional<Component> findById(ComponentId componentId);

    /**
     * Content ID로 컴포넌트 목록 조회
     *
     * @param contentId Content ID
     * @return 컴포넌트 목록 (sortOrder 순 정렬)
     */
    List<Component> findByContentId(ContentId contentId);

    /**
     * 컴포넌트 존재 여부 확인
     *
     * @param componentId 컴포넌트 ID
     * @return 존재 여부
     */
    boolean existsById(ComponentId componentId);
}
