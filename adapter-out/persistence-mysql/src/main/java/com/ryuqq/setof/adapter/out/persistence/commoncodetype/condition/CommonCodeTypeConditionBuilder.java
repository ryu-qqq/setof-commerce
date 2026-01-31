package com.ryuqq.setof.adapter.out.persistence.commoncodetype.condition;

import static com.ryuqq.setof.adapter.out.persistence.commoncodetype.entity.QCommonCodeTypeJpaEntity.commonCodeTypeJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CommonCodeTypeConditionBuilder - 공통 코드 타입 QueryDSL 조건 빌더.
 *
 * <p>BooleanExpression 조건을 재사용 가능한 형태로 제공합니다.
 *
 * <p>PER-CND-001: BooleanExpression은 ConditionBuilder로 분리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CommonCodeTypeConditionBuilder {

    /**
     * ID 일치 조건.
     *
     * @param id 공통 코드 타입 ID
     * @return ID 일치 조건
     */
    public BooleanExpression idEq(Long id) {
        return id != null ? commonCodeTypeJpaEntity.id.eq(id) : null;
    }

    /**
     * ID 목록 일치 조건.
     *
     * @param ids ID 목록
     * @return ID 목록 일치 조건
     */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? commonCodeTypeJpaEntity.id.in(ids) : null;
    }

    /**
     * ID 제외 조건.
     *
     * @param excludeId 제외할 ID
     * @return ID 제외 조건
     */
    public BooleanExpression idNe(Long excludeId) {
        return excludeId != null ? commonCodeTypeJpaEntity.id.ne(excludeId) : null;
    }

    /**
     * 코드 일치 조건.
     *
     * @param code 코드
     * @return 코드 일치 조건
     */
    public BooleanExpression codeEq(String code) {
        return code != null ? commonCodeTypeJpaEntity.code.eq(code) : null;
    }

    /**
     * 표시 순서 일치 조건.
     *
     * @param displayOrder 표시 순서
     * @return 표시 순서 일치 조건
     */
    public BooleanExpression displayOrderEq(Integer displayOrder) {
        return displayOrder != null ? commonCodeTypeJpaEntity.displayOrder.eq(displayOrder) : null;
    }

    /**
     * 활성화 상태 일치 조건.
     *
     * @param active 활성화 상태 (null이면 전체)
     * @return active 일치 조건 (nullable이면 null 반환)
     */
    public BooleanExpression activeEq(Boolean active) {
        return active != null ? commonCodeTypeJpaEntity.active.eq(active) : null;
    }

    /**
     * 활성화 상태 조건 (Criteria 기반).
     *
     * @param criteria 검색 조건
     * @return active 일치 조건 (필터가 없으면 null 반환)
     */
    public BooleanExpression activeEq(CommonCodeTypeSearchCriteria criteria) {
        return criteria.hasActiveFilter()
                ? commonCodeTypeJpaEntity.active.eq(criteria.active())
                : null;
    }

    /**
     * 키워드 검색 조건 (코드, 이름에서 검색).
     *
     * @param keyword 검색 키워드 (null 또는 빈 문자열이면 null 반환)
     * @return 키워드 검색 조건
     */
    public BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return commonCodeTypeJpaEntity
                .code
                .containsIgnoreCase(keyword)
                .or(commonCodeTypeJpaEntity.name.containsIgnoreCase(keyword))
                .or(commonCodeTypeJpaEntity.description.containsIgnoreCase(keyword));
    }

    /**
     * 키워드 검색 조건 (Criteria 기반).
     *
     * @param criteria 검색 조건
     * @return 키워드 검색 조건 (키워드가 없으면 null 반환)
     */
    public BooleanExpression keywordContains(CommonCodeTypeSearchCriteria criteria) {
        return criteria.hasKeyword() ? keywordContains(criteria.keyword()) : null;
    }
}
