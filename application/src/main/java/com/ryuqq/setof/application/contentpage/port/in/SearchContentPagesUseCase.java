package com.ryuqq.setof.application.contentpage.port.in;

import com.ryuqq.setof.application.contentpage.dto.ContentPagePageResult;
import com.ryuqq.setof.application.contentpage.dto.ContentPageSearchParams;

/**
 * SearchContentPagesUseCase - 콘텐츠 페이지 목록 검색 유즈케이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface SearchContentPagesUseCase {

    ContentPagePageResult execute(ContentPageSearchParams params);
}
