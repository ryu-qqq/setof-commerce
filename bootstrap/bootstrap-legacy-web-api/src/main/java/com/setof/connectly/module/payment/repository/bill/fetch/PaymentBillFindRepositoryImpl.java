package com.setof.connectly.module.payment.repository.bill.fetch;

import static com.setof.connectly.module.payment.entity.QPaymentBill.paymentBill;
import static com.setof.connectly.module.payment.entity.QPaymentMethod.paymentMethod;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.payment.dto.payment.FailPaymentResponse;
import com.setof.connectly.module.payment.dto.payment.QFailPaymentResponse;
import com.setof.connectly.module.payment.entity.PaymentBill;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentBillFindRepositoryImpl implements PaymentBillFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PaymentBill> fetchPaymentBillEntity(long paymentId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(paymentBill).where(isPaymentIdEq(paymentId)).fetchFirst());
    }

    @Override
    public Optional<PaymentBill> fetchPaymentBillEntityByUniqueId(String paymentUniqueId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(paymentBill)
                        .where(isPaymentUniqueIdEq(paymentUniqueId))
                        .fetchFirst());
    }

    @Override
    public Optional<FailPaymentResponse> fetchPaymentMethod(String paymentUniqueId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QFailPaymentResponse(
                                        paymentBill.paymentId,
                                        paymentMethod.paymentMethodEnum,
                                        paymentBill.paymentAmount))
                        .from(paymentBill)
                        .innerJoin(paymentMethod)
                        .on(paymentMethod.id.eq(paymentBill.paymentMethodId))
                        .where(isPaymentUniqueIdEq(paymentUniqueId), userIdEq(userId))
                        .fetchOne());
    }

    @Override
    public Optional<String> fetchPaymentAgencyId(long paymentId) {
        return Optional.ofNullable(
                queryFactory
                        .select(paymentBill.paymentAgencyId)
                        .from(paymentBill)
                        .where(isPaymentIdEq(paymentId))
                        .fetchOne());
    }

    private BooleanExpression isPaymentIdEq(long paymentId) {
        return paymentBill.paymentId.eq(paymentId);
    }

    private BooleanExpression isPaymentUniqueIdEq(String paymentUniqueId) {
        return paymentBill.paymentUniqueId.eq(paymentUniqueId);
    }

    private BooleanExpression userIdEq(long userId) {
        return paymentBill.userId.eq(userId);
    }
}
