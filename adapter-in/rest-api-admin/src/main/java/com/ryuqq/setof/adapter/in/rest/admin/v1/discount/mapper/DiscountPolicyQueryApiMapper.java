package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request.DiscountPolicySearchV1ApiRequest;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyQueryApiMapper - 할인 정책 Query API 변환 매퍼.
 *
 * <p>v1 레거시 검색 요청 DTO와 Application 검색 파라미터 간 변환을 담당합니다.
 *
 * <p>레거시 필드 매핑 규칙:
 *
 * <ul>
 *   <li>publisherType → publisherType (그대로)
 *   <li>activeYn "Y" → active true, "N" → active false, null → active null
 *   <li>page/size → withDefaults() 적용 후 전달
 *   <li>sortBy → sortKey
 *   <li>sortDirection → sortDirection (그대로)
 *   <li>issueType → applicationType 매핑 불가, null 처리
 *   <li>periodType, userId, searchKeyword, searchWord, startDate, endDate → 새 시스템에 없으므로 무시
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyQueryApiMapper {

    /**
     * DiscountPolicySearchV1ApiRequest → DiscountPolicySearchParams 변환.
     *
     * <p>레거시 issueType은 새 시스템의 applicationType으로 직접 매핑할 수 없어 null로 처리합니다. periodType, userId,
     * searchKeyword, searchWord, startDate, endDate는 새 시스템 SearchParams에 해당 필드가 없으므로 무시합니다.
     *
     * @param request v1 할인 정책 검색 요청 DTO
     * @return Application 검색 파라미터 DTO
     */
    public DiscountPolicySearchParams toSearchParams(DiscountPolicySearchV1ApiRequest request) {
        DiscountPolicySearchV1ApiRequest normalized = request.withDefaults();
        return new DiscountPolicySearchParams(
                null,
                normalized.publisherType(),
                null,
                null,
                toActiveBoolean(normalized.activeYn()),
                normalized.sortBy(),
                normalized.sortDirection(),
                normalized.page(),
                normalized.size());
    }

    /**
     * 레거시 activeYn → Boolean 변환.
     *
     * <p>"Y"이면 true, "N"이면 false, null이면 null 반환.
     *
     * @param activeYn 레거시 활성화 여부 ("Y" / "N" / null)
     * @return Boolean 변환 결과 (null 허용)
     */
    private Boolean toActiveBoolean(String activeYn) {
        if (activeYn == null) {
            return null;
        }
        return "Y".equalsIgnoreCase(activeYn);
    }
}
