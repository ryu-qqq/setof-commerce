package com.ryuqq.setof.adapter.out.persistence.memberauth.condition;

import static com.ryuqq.setof.adapter.out.persistence.memberauth.entity.QMemberAuthJpaEntity.memberAuthJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * MemberAuthConditionBuilder - 회원 인증 수단 QueryDSL 조건 빌더.
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
public class MemberAuthConditionBuilder {

    /** 회원 ID 일치 조건 */
    public BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? memberAuthJpaEntity.memberId.eq(memberId) : null;
    }

    /** 인증 제공자 일치 조건 */
    public BooleanExpression authProviderEq(String authProvider) {
        return authProvider != null && !authProvider.isBlank()
                ? memberAuthJpaEntity.authProvider.eq(authProvider)
                : null;
    }

    /** 제공자 사용자 ID 일치 조건 */
    public BooleanExpression providerUserIdEq(String providerUserId) {
        return providerUserId != null && !providerUserId.isBlank()
                ? memberAuthJpaEntity.providerUserId.eq(providerUserId)
                : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return memberAuthJpaEntity.deletedAt.isNull();
    }
}
