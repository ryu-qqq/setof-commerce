package com.ryuqq.setof.application.contentpage.service;

import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageMetaUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import org.springframework.stereotype.Service;

/**
 * 콘텐츠 페이지 메타데이터 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetContentPageMetaService implements GetContentPageMetaUseCase {

    private final ContentPageQueryManager queryManager;

    public GetContentPageMetaService(ContentPageQueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public ContentPage execute(long contentPageId) {
        return queryManager.findByIdOrThrow(contentPageId);
    }
}
