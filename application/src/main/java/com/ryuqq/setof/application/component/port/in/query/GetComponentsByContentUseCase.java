package com.ryuqq.setof.application.component.port.in.query;

import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;

/**
 * Content별 Component 목록 조회 UseCase (Query)
 *
 * <p>특정 Content에 속한 컴포넌트 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetComponentsByContentUseCase {

    /**
     * Content에 속한 컴포넌트 목록 조회
     *
     * @param contentId Content ID
     * @return 컴포넌트 목록
     */
    List<ComponentResponse> getByContentId(ContentId contentId);
}
