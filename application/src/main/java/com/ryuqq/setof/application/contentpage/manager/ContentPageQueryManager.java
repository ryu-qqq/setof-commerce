package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.contentpage.port.out.ContentPageQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.exception.ContentPageNotFoundException;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
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
    public Set<Long> fetchOnDisplayContentPageIds() {
        return queryPort.fetchOnDisplayContentPageIds();
    }

    @Transactional(readOnly = true)
    public ContentPage fetchContentPageMeta(long contentPageId) {
        return queryPort
                .fetchContentPageMeta(contentPageId)
                .orElseThrow(() -> new ContentPageNotFoundException(String.valueOf(contentPageId)));
    }

    @Transactional(readOnly = true)
    public ContentPage fetchContentPage(ContentPageSearchCriteria criteria) {
        return queryPort
                .fetchContentPage(criteria)
                .orElseThrow(
                        () ->
                                new ContentPageNotFoundException(
                                        String.valueOf(criteria.contentPageId())));
    }
}
