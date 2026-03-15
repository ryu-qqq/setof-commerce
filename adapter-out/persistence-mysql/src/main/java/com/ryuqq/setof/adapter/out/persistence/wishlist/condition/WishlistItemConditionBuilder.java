package com.ryuqq.setof.adapter.out.persistence.wishlist.condition;

import static com.ryuqq.setof.adapter.out.persistence.wishlist.entity.QWishlistItemJpaEntity.wishlistItemJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * WishlistItemConditionBuilder - 찜 항목 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistItemConditionBuilder {

    /** 레거시 회원 ID 일치 조건 */
    public BooleanExpression legacyMemberIdEq(Long legacyMemberId) {
        return legacyMemberId != null
                ? wishlistItemJpaEntity.legacyMemberId.eq(legacyMemberId)
                : null;
    }

    /** 상품 그룹 ID 일치 조건 */
    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null
                ? wishlistItemJpaEntity.productGroupId.eq(productGroupId)
                : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return wishlistItemJpaEntity.deletedAt.isNull();
    }

    /** 커서 기준 이전 항목 조건 (id < cursor, DESC 정렬) */
    public BooleanExpression cursorLessThan(Long cursor) {
        return cursor != null ? wishlistItemJpaEntity.id.lt(cursor) : null;
    }
}
