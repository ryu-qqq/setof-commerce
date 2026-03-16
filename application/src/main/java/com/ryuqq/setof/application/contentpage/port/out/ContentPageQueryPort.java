package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * ContentPageQueryPort - 콘텐츠 페이지 조회 출력 포트.
 *
 * <p>APP-PRT-001: Port-Out은 interface이며, Adapter-Out이 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ContentPageQueryPort {

    /**
     * 전시 중인 콘텐츠 페이지 ID 목록 조회.
     *
     * @return 전시 중인 콘텐츠 페이지 ID Set
     */
    Set<Long> findOnDisplayContentPageIds();

    /**
     * ID로 콘텐츠 페이지 메타데이터 조회 (컴포넌트 없이).
     *
     * @param contentPageId 콘텐츠 페이지 ID
     * @return ContentPage Optional
     */
    Optional<ContentPage> findById(long contentPageId);

    /**
     * 콘텐츠 페이지 상세 조회 (컴포넌트 포함).
     *
     * @param criteria 검색 조건
     * @return ContentPage Optional
     */
    Optional<ContentPage> findByCriteria(ContentPageSearchCriteria criteria);

    /**
     * 검색 조건에 해당하는 콘텐츠 페이지 목록 조회.
     *
     * @param criteria 목록 검색 조건
     * @return 콘텐츠 페이지 목록
     */
    List<ContentPage> findAllByCriteria(ContentPageListSearchCriteria criteria);

    /**
     * 검색 조건에 해당하는 콘텐츠 페이지 수.
     *
     * @param criteria 목록 검색 조건
     * @return 콘텐츠 페이지 수
     */
    long countByCriteria(ContentPageListSearchCriteria criteria);
}
