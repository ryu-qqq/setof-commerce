package com.ryuqq.setof.application.component.manager.query;

import com.ryuqq.setof.application.component.port.out.query.ComponentQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.exception.ComponentNotFoundException;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component 조회 Manager (Query)
 *
 * <p>컴포넌트 조회 트랜잭션 경계 관리 및 도메인 예외 변환
 *
 * @author development-team
 * @since 1.0.0
 */
@org.springframework.stereotype.Component
@Transactional(readOnly = true)
public class ComponentReadManager {

    private final ComponentQueryPort componentQueryPort;

    public ComponentReadManager(ComponentQueryPort componentQueryPort) {
        this.componentQueryPort = componentQueryPort;
    }

    /**
     * 컴포넌트 ID로 조회
     *
     * @param componentId 컴포넌트 ID
     * @return 컴포넌트
     * @throws ComponentNotFoundException 컴포넌트가 존재하지 않는 경우
     */
    public Component findById(ComponentId componentId) {
        return componentQueryPort
                .findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId));
    }

    /**
     * Content ID로 컴포넌트 목록 조회
     *
     * @param contentId Content ID
     * @return 컴포넌트 목록 (sortOrder 순 정렬)
     */
    public List<Component> findByContentId(ContentId contentId) {
        return componentQueryPort.findByContentId(contentId);
    }

    /**
     * 컴포넌트 존재 여부 확인
     *
     * @param componentId 컴포넌트 ID
     * @return 존재 여부
     */
    public boolean existsById(ComponentId componentId) {
        return componentQueryPort.existsById(componentId);
    }
}
