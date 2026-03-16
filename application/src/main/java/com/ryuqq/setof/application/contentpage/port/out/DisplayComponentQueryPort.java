package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.List;

/**
 * DisplayComponentQueryPort - 디스플레이 컴포넌트 조회 출력 포트.
 *
 * <p>APP-PRT-001: Port-Out은 interface이며, Adapter-Out이 구현합니다.
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
    List<DisplayComponent> findByContentPage(ContentPageSearchCriteria criteria);
}
