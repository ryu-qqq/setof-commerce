package com.ryuqq.setof.adapter.out.persistence.memberconsent.condition;

import static com.ryuqq.setof.adapter.out.persistence.memberconsent.entity.QMemberConsentJpaEntity.memberConsentJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * MemberConsentConditionBuilder - 회원 동의 정보 QueryDSL 조건 빌더.
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
public class MemberConsentConditionBuilder {

    /** 회원 ID 일치 조건 */
    public BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? memberConsentJpaEntity.memberId.eq(memberId) : null;
    }
}
