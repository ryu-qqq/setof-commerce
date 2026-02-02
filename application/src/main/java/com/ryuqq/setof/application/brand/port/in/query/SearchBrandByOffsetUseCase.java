package com.ryuqq.setof.application.brand.port.in.query;

import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;

/**
 * 브랜드 검색 UseCase (Offset 기반 페이징).
 *
 * <p>APP-UC-001: Offset 페이징은 Search{Domain}ByOffsetUseCase.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SearchBrandByOffsetUseCase {

    /**
     * 브랜드를 검색합니다.
     *
     * @param params 검색 파라미터
     * @return 브랜드 페이지 결과
     */
    BrandPageResult execute(BrandSearchParams params);
}
