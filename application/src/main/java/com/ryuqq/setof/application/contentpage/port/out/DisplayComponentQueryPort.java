package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.List;

/**
 * DisplayComponentQueryPort - 디스플레이 컴포넌트 조회 출력 포트.
 *
 * <p>컴포넌트 메타 + ViewExtension + ComponentSpec 조회를 담당한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DisplayComponentQueryPort {

    /**
     * 콘텐츠 페이지의 컴포넌트 목록 조회 (ComponentSpec 포함).
     *
     * @param criteria 검색 조건
     * @return DisplayComponent 목록
     */
    List<DisplayComponent> fetchDisplayComponents(ContentPageSearchCriteria criteria);
}
