package com.ryuqq.setof.application.contentpage.service;

import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.port.in.GetOnDisplayContentPageIdsUseCase;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * 전시 중인 콘텐츠 페이지 ID 목록 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetOnDisplayContentPageIdsService implements GetOnDisplayContentPageIdsUseCase {

    private final ContentPageQueryManager queryManager;

    public GetOnDisplayContentPageIdsService(ContentPageQueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public Set<Long> execute() {
        return queryManager.fetchOnDisplayContentPageIds();
    }
}
