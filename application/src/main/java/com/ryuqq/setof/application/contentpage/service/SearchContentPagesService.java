package com.ryuqq.setof.application.contentpage.service;

import com.ryuqq.setof.application.contentpage.dto.ContentPagePageResult;
import com.ryuqq.setof.application.contentpage.dto.ContentPageSearchParams;
import com.ryuqq.setof.application.contentpage.factory.ContentPageQueryFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.port.in.SearchContentPagesUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * SearchContentPagesService - 콘텐츠 페이지 목록 검색 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SearchContentPagesService implements SearchContentPagesUseCase {

    private final ContentPageQueryManager queryManager;
    private final ContentPageQueryFactory queryFactory;

    public SearchContentPagesService(
            ContentPageQueryManager queryManager, ContentPageQueryFactory queryFactory) {
        this.queryManager = queryManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public ContentPagePageResult execute(ContentPageSearchParams params) {
        ContentPageListSearchCriteria criteria = queryFactory.create(params);
        List<ContentPage> pages = queryManager.findByCriteria(criteria);
        long totalCount = queryManager.countByCriteria(criteria);

        Long lastDomainId = pages.isEmpty() ? null : pages.get(pages.size() - 1).idValue();

        return ContentPagePageResult.of(
                pages, totalCount, criteria.page(), criteria.size(), lastDomainId);
    }
}
