package com.ryuqq.setof.application.contentpage.port.in;

import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;

/**
 * GetContentPageDetailUseCase - 콘텐츠 페이지 상세 조회 유즈케이스.
 *
 * <p>콘텐츠 메타 + 컴포넌트 상세를 하나의 결과로 반환한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetContentPageDetailUseCase {

    ContentPageDetailResult execute(ContentPageSearchCriteria criteria);
}
