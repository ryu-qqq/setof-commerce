package com.ryuqq.setof.application.content.manager.query;

import com.ryuqq.setof.application.content.port.out.query.ContentQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.exception.ContentNotFoundException;
import com.ryuqq.setof.domain.cms.query.criteria.ContentSearchCriteria;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Content Read Manager
 *
 * <p>Content 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentReadManager {

    private final ContentQueryPort contentQueryPort;

    public ContentReadManager(ContentQueryPort contentQueryPort) {
        this.contentQueryPort = contentQueryPort;
    }

    /**
     * ID로 Content 조회 (없으면 예외)
     *
     * @param contentId 콘텐츠 ID
     * @return Content
     * @throws ContentNotFoundException 콘텐츠가 존재하지 않으면
     */
    public Content findById(Long contentId) {
        ContentId id = ContentId.of(contentId);
        return contentQueryPort
                .findById(id)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
    }

    /**
     * 검색 조건으로 Content 목록 조회
     *
     * @param criteria 검색 조건
     * @return Content 목록
     */
    public List<Content> findByCriteria(ContentSearchCriteria criteria) {
        return contentQueryPort.findByCriteria(criteria);
    }

    /**
     * 검색 조건에 맞는 Content 총 개수 조회
     *
     * @param criteria 검색 조건
     * @return 총 개수
     */
    public long countByCriteria(ContentSearchCriteria criteria) {
        return contentQueryPort.countByCriteria(criteria);
    }

    /**
     * Content 존재 여부 확인
     *
     * @param contentId 콘텐츠 ID
     * @return 존재 여부
     */
    public boolean existsById(Long contentId) {
        ContentId id = ContentId.of(contentId);
        return contentQueryPort.existsById(id);
    }
}
