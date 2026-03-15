package com.ryuqq.setof.application.contentpage.factory;

import com.ryuqq.setof.application.contentpage.dto.ContentPageSearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSortKey;
import org.springframework.stereotype.Component;

/**
 * ContentPageQueryFactory - 콘텐츠 페이지 검색 조건 변환 Factory.
 *
 * <p>{@link ContentPageSearchParams}를 도메인 레이어의 {@link ContentPageListSearchCriteria}로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageQueryFactory {

    /**
     * SearchParams → ListSearchCriteria 변환.
     *
     * @param params 외부 검색 파라미터
     * @return 도메인 검색 조건
     */
    public ContentPageListSearchCriteria create(ContentPageSearchParams params) {
        PageRequest pageRequest = PageRequest.of(params.page(), params.size());
        QueryContext<ContentPageSortKey> queryContext =
                QueryContext.of(ContentPageSortKey.defaultKey(), SortDirection.DESC, pageRequest);

        return new ContentPageListSearchCriteria(
                params.active(),
                params.displayPeriodStart(),
                params.displayPeriodEnd(),
                params.createdAtStart(),
                params.createdAtEnd(),
                params.titleKeyword(),
                params.contentPageId(),
                params.lastDomainId(),
                queryContext);
    }
}
