package com.ryuqq.setof.application.content.port.out.query;

import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.query.criteria.ContentSearchCriteria;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import java.util.Optional;

/**
 * Content Query Port (Query)
 *
 * <p>Content Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ContentQueryPort {

    /**
     * ID로 Content 조회
     *
     * @param contentId 콘텐츠 ID
     * @return Content (없으면 empty)
     */
    Optional<Content> findById(ContentId contentId);

    /**
     * 검색 조건으로 Content 목록 조회
     *
     * @param criteria 검색 조건
     * @return Content 목록
     */
    List<Content> findByCriteria(ContentSearchCriteria criteria);

    /**
     * 검색 조건에 맞는 Content 총 개수 조회
     *
     * @param criteria 검색 조건
     * @return 총 개수
     */
    long countByCriteria(ContentSearchCriteria criteria);

    /**
     * Content 존재 여부 확인
     *
     * @param contentId 콘텐츠 ID
     * @return 존재 여부
     */
    boolean existsById(ContentId contentId);
}
