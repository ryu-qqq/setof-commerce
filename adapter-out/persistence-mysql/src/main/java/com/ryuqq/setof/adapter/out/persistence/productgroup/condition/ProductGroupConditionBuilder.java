package com.ryuqq.setof.adapter.out.persistence.productgroup.condition;

import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity.productGroupJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupConditionBuilder - 상품 그룹 QueryDSL 조건 빌더.
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
public class ProductGroupConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? productGroupJpaEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? productGroupJpaEntity.id.in(ids) : null;
    }

    /** 셀러 ID 목록 포함 조건 */
    public BooleanExpression sellerIdIn(List<Long> sellerIds) {
        return sellerIds != null && !sellerIds.isEmpty()
                ? productGroupJpaEntity.sellerId.in(sellerIds)
                : null;
    }

    /** 브랜드 ID 목록 포함 조건 */
    public BooleanExpression brandIdIn(List<Long> brandIds) {
        return brandIds != null && !brandIds.isEmpty()
                ? productGroupJpaEntity.brandId.in(brandIds)
                : null;
    }

    /** 카테고리 ID 목록 포함 조건 */
    public BooleanExpression categoryIdIn(List<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty()
                ? productGroupJpaEntity.categoryId.in(categoryIds)
                : null;
    }

    /** DELETED 상태가 아닌 조건 */
    public BooleanExpression statusNotDeleted() {
        return productGroupJpaEntity.status.ne("DELETED");
    }
}
