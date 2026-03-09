package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.Optional;
import java.util.Set;

/**
 * ContentPageQueryPort - 콘텐츠 페이지 조회 출력 포트.
 *
 * <p>Persistence Adapter가 구현하는 출력 포트 인터페이스. 레거시 DB를 조회하여 도메인 객체로 변환 후 반환한다.
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
    Set<Long> fetchOnDisplayContentPageIds();

    /**
     * 콘텐츠 페이지 메타데이터 조회 (컴포넌트 없이).
     *
     * @param contentPageId 콘텐츠 페이지 ID
     * @return ContentPage Optional
     */
    Optional<ContentPage> fetchContentPageMeta(long contentPageId);

    /**
     * 콘텐츠 페이지 상세 조회 (컴포넌트 포함).
     *
     * @param criteria 검색 조건
     * @return ContentPage Optional
     */
    Optional<ContentPage> fetchContentPage(ContentPageSearchCriteria criteria);
}
