package com.ryuqq.setof.application.content.service.query;

import com.ryuqq.setof.application.content.assembler.ContentAssembler;
import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import com.ryuqq.setof.application.content.factory.query.ContentQueryFactory;
import com.ryuqq.setof.application.content.manager.query.ContentReadManager;
import com.ryuqq.setof.application.content.port.in.query.GetContentUseCase;
import com.ryuqq.setof.application.content.port.in.query.SearchContentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.query.criteria.ContentSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Content 조회 Service
 *
 * <p>콘텐츠 단건 및 목록 조회를 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ContentQueryService implements GetContentUseCase, SearchContentUseCase {

    private final ContentReadManager contentReadManager;
    private final ContentQueryFactory contentQueryFactory;
    private final ContentAssembler contentAssembler;

    public ContentQueryService(
            ContentReadManager contentReadManager,
            ContentQueryFactory contentQueryFactory,
            ContentAssembler contentAssembler) {
        this.contentReadManager = contentReadManager;
        this.contentQueryFactory = contentQueryFactory;
        this.contentAssembler = contentAssembler;
    }

    @Override
    public ContentResponse execute(Long contentId) {
        Content content = contentReadManager.findById(contentId);
        return contentAssembler.toResponse(content);
    }

    @Override
    public List<ContentSummaryResponse> execute(SearchContentQuery query) {
        ContentSearchCriteria criteria = contentQueryFactory.create(query);
        List<Content> contents = contentReadManager.findByCriteria(criteria);
        return contentAssembler.toSummaryResponses(contents);
    }

    @Override
    public long count(SearchContentQuery query) {
        ContentSearchCriteria criteria = contentQueryFactory.create(query);
        return contentReadManager.countByCriteria(criteria);
    }
}
