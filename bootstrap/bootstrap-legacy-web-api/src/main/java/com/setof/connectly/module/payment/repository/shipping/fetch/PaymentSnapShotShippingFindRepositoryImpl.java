package com.setof.connectly.module.payment.repository.shipping.fetch;

import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotShippingAddress.paymentSnapShotShippingAddress;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentSnapShotShippingFindRepositoryImpl
        implements PaymentSnapShotShippingFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> fetchPaymentSnapShotShippingAddressId(long paymentId) {
        return Optional.ofNullable(
                queryFactory
                        .select(paymentSnapShotShippingAddress.id)
                        .from(paymentSnapShotShippingAddress)
                        .where(PaymentIdEq(paymentId))
                        .fetchFirst());
    }

    private BooleanExpression PaymentIdEq(long paymentId) {
        return paymentSnapShotShippingAddress.paymentId.eq(paymentId);
    }
}
