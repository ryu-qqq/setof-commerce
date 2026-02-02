package com.ryuqq.setof.adapter.out.persistence.brand.condition;

import static com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity.brandJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BrandConditionBuilder - 브랜드 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? brandJpaEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? brandJpaEntity.id.in(ids) : null;
    }

    /** 브랜드명 포함 조건 */
    public BooleanExpression brandNameContains(String brandName) {
        return brandName != null && !brandName.isBlank()
                ? brandJpaEntity.brandName.containsIgnoreCase(brandName)
                : null;
    }

    /** 표시 여부 일치 조건 */
    public BooleanExpression displayedEq(Boolean displayed) {
        return displayed != null ? brandJpaEntity.displayed.eq(displayed) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return brandJpaEntity.deletedAt.isNull();
    }
}
