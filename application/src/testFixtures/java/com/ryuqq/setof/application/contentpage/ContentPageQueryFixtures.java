package com.ryuqq.setof.application.contentpage;

import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;

/**
 * ContentPage Application Query 테스트 Fixtures.
 *
 * <p>ContentPageQueryManager, DisplayComponentReadManager, ContentPageDetailReadFacade 테스트에서 공통으로
 * 사용하는 쿼리 관련 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentPageQueryFixtures {

    private ContentPageQueryFixtures() {}

    // ===== ContentPageSearchCriteria =====

    public static ContentPageSearchCriteria searchCriteria(long contentPageId) {
        return new ContentPageSearchCriteria(contentPageId, false);
    }

    public static ContentPageSearchCriteria searchCriteriaWithBypass(long contentPageId) {
        return new ContentPageSearchCriteria(contentPageId, true);
    }

    public static ContentPageSearchCriteria defaultSearchCriteria() {
        return searchCriteria(1L);
    }
}
