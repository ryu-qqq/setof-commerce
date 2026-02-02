package com.ryuqq.setof.application.brand.port.in.query;

import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import java.util.List;

/**
 * 노출용 브랜드 조회 UseCase.
 *
 * <p>Public API용 간단한 브랜드 목록 조회입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetBrandsForDisplayUseCase {

    /**
     * 노출용 브랜드 목록을 조회합니다.
     *
     * @param params 검색 파라미터
     * @return 브랜드 목록
     */
    List<BrandDisplayResult> execute(BrandDisplaySearchParams params);
}
