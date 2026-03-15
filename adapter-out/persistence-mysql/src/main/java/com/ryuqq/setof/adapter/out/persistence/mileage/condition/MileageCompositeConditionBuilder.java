package com.ryuqq.setof.adapter.out.persistence.mileage.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.mileage.entity.QMileageEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.mileage.entity.QMileageLedgerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.mileage.entity.QMileageTransactionJpaEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * MileageCompositeConditionBuilder - 마일리지 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MileageCompositeConditionBuilder {

    private static final QMileageLedgerJpaEntity ledger =
            QMileageLedgerJpaEntity.mileageLedgerJpaEntity;
    private static final QMileageEntryJpaEntity entry =
            QMileageEntryJpaEntity.mileageEntryJpaEntity;
    private static final QMileageTransactionJpaEntity transaction =
            QMileageTransactionJpaEntity.mileageTransactionJpaEntity;

    /** 원장의 회원 ID 일치 조건. */
    public BooleanExpression memberIdEq(long userId) {
        return ledger.memberId.eq(userId);
    }

    /** 엔트리 상태가 ACTIVE인 조건. */
    public BooleanExpression entryStatusActive() {
        return entry.status.eq("ACTIVE");
    }

    /** 엔트리 만료일이 지정 일시 이전인 조건. */
    public BooleanExpression entryExpirationBefore(LocalDateTime dateTime) {
        return entry.expirationDate.isNotNull().and(entry.expirationDate.loe(dateTime));
    }

    /** 엔트리 만료일이 지정 일시 이후인 조건. */
    public BooleanExpression entryExpirationAfter(LocalDateTime dateTime) {
        return entry.expirationDate.isNotNull().and(entry.expirationDate.gt(dateTime));
    }

    /** 거래 사유 필터 조건. */
    public BooleanExpression transactionReasonIn(List<String> reasons) {
        return reasons != null && !reasons.isEmpty() ? transaction.reason.in(reasons) : null;
    }
}
