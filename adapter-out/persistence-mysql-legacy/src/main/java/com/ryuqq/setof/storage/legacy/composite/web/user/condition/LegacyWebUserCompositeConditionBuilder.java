package com.ryuqq.setof.storage.legacy.composite.web.user.condition;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserGradeEntity.legacyUserGradeEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserMileageEntity.legacyUserMileageEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * 레거시 사용자 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebUserCompositeConditionBuilder {

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyUserEntity.id.eq(userId) : null;
    }

    public BooleanExpression phoneNumberEq(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }
        return legacyUserEntity.phoneNumber.eq(phoneNumber);
    }

    public BooleanExpression userGradeJoinCondition() {
        return legacyUserGradeEntity.id.eq(legacyUserEntity.userGradeId);
    }

    public BooleanExpression userMileageJoinCondition() {
        return legacyUserMileageEntity.id.eq(legacyUserEntity.id);
    }
}
