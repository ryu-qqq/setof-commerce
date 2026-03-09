package com.ryuqq.setof.application.contentpage.port.in;

import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;

/**
 * 콘텐츠 페이지 메타데이터 조회 UseCase.
 *
 * <p>컴포넌트 없이 콘텐츠 페이지 메타 정보만 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetContentPageMetaUseCase {

    /**
     * 콘텐츠 페이지 메타데이터를 조회합니다.
     *
     * @param contentPageId 콘텐츠 페이지 ID
     * @return ContentPage
     */
    ContentPage execute(long contentPageId);
}
