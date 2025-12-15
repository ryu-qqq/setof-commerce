package com.setof.connectly.module.mileage.repository.fetch;

import static com.setof.connectly.module.mileage.entity.QMileageTransaction.mileageTransaction;
import static com.setof.connectly.module.order.entity.order.QOrder.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MileageTransactionFindRepositoryImpl implements MileageTransactionFindRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<MileageTransaction> fetchMileageTransactionEntity(long orderId) {
        return Optional.ofNullable(
                queryFactory
                        .select(mileageTransaction)
                        .from(order)
                        .innerJoin(mileageTransaction)
                        .on(mileageTransaction.targetId.eq(order.id))
                        .on(mileageTransaction.issueType.eq(MileageIssueType.ORDER))
                        .where(orderIdEq(orderId), issueTypeEq())
                        .fetchFirst());
    }

    private BooleanExpression orderIdEq(long orderId) {
        return order.id.eq(orderId);
    }

    private BooleanExpression issueTypeEq() {
        return mileageTransaction.issueType.eq(MileageIssueType.ORDER);
    }
}
