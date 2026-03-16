package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.contentpage.port.out.ContentPageQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.exception.ContentPageNotFoundException;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ContentPageQueryManager - 콘텐츠 페이지 조회 매니저.
 *
 * <p>Port를 통해 콘텐츠 페이지를 조회하고, 존재하지 않을 경우 예외를 발생시킨다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageQueryManager {

    private final ContentPageQueryPort queryPort;

    public ContentPageQueryManager(ContentPageQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Set<Long> findOnDisplayContentPageIds() {
        return queryPort.findOnDisplayContentPageIds();
    }

    @Transactional(readOnly = true)
    public ContentPage findByIdOrThrow(long contentPageId) {
        return queryPort
                .findById(contentPageId)
                .orElseThrow(() -> new ContentPageNotFoundException(String.valueOf(contentPageId)));
    }

    @Transactional(readOnly = true)
    public ContentPage findByCriteriaOrThrow(ContentPageSearchCriteria criteria) {
        return queryPort
                .findByCriteria(criteria)
                .orElseThrow(
                        () ->
                                new ContentPageNotFoundException(
                                        String.valueOf(criteria.contentPageId())));
    }

    /**
     * 검색 조건으로 콘텐츠 페이지 목록을 조회합니다.
     *
     * @param criteria 목록 검색 조건
     * @return 콘텐츠 페이지 목록
     */
    @Transactional(readOnly = true)
    public List<ContentPage> findAllByCriteria(ContentPageListSearchCriteria criteria) {
        return queryPort.findAllByCriteria(criteria);
    }

    /**
     * 검색 조건에 해당하는 콘텐츠 페이지 수를 반환합니다.
     *
     * @param criteria 목록 검색 조건
     * @return 콘텐츠 페이지 수
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContentPageListSearchCriteria criteria) {
        return queryPort.countByCriteria(criteria);
    }
}
