package com.ryuqq.setof.adapter.out.persistence.member.condition;

import static com.ryuqq.setof.adapter.out.persistence.member.entity.QMemberJpaEntity.memberJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * MemberConditionBuilder - 회원 QueryDSL 조건 빌더.
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
public class MemberConditionBuilder {

    /** 회원 ID 일치 조건. */
    public BooleanExpression idEq(Long id) {
        return id != null ? memberJpaEntity.id.eq(id) : null;
    }

    /** 전화번호 일치 조건 */
    public BooleanExpression phoneNumberEq(String phoneNumber) {
        return phoneNumber != null && !phoneNumber.isBlank()
                ? memberJpaEntity.phoneNumber.eq(phoneNumber)
                : null;
    }

    /** 상태 일치 조건 */
    public BooleanExpression statusEq(String status) {
        return status != null && !status.isBlank() ? memberJpaEntity.status.eq(status) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return memberJpaEntity.deletedAt.isNull();
    }
}
