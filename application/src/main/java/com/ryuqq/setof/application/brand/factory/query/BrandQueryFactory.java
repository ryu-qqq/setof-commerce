package com.ryuqq.setof.application.brand.factory.query;

import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import org.springframework.stereotype.Component;

/**
 * Brand Query Factory
 *
 * <p>Application Layer의 Query DTO를 Domain Criteria로 변환하는 Factory
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>BrandSearchQuery → BrandSearchCriteria 변환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Port/Repository 의존 금지 (순수 변환만)
 *   <li>비즈니스 로직 금지
 *   <li>데이터 조회 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandQueryFactory {

    /**
     * BrandSearchQuery → BrandSearchCriteria 변환
     *
     * @param query Application Layer 검색 조건
     * @return Domain Layer Criteria
     */
    public BrandSearchCriteria createCriteria(BrandSearchQuery query) {
        return BrandSearchCriteria.of(query.keyword(), query.status(), query.page(), query.size());
    }
}
