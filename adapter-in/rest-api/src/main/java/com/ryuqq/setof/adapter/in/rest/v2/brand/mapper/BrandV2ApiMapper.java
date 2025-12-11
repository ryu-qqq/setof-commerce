package com.ryuqq.setof.adapter.in.rest.v2.brand.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.query.BrandV2SearchApiRequest;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import org.springframework.stereotype.Component;

/**
 * Brand V2 API Mapper
 *
 * <p>REST API Request/Response 변환을 담당하는 Mapper입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>API Request → Application Query DTO 변환
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class BrandV2ApiMapper {

    /**
     * BrandV2SearchApiRequest → BrandSearchQuery 변환
     *
     * @param request API 요청
     * @return Application Layer 검색 조건
     */
    public BrandSearchQuery toSearchQuery(BrandV2SearchApiRequest request) {
        return BrandSearchQuery.ofKeyword(request.keyword(), request.page(), request.size());
    }
}
