package com.setof.connectly.module.user.repository.refund;

import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotRefundAccount.paymentSnapShotRefundAccount;
import static com.setof.connectly.module.user.entity.QRefundAccount.refundAccount;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.dto.account.QRefundAccountInfo;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefundAccountFindRepositoryImpl implements RefundAccountFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<RefundAccountInfo> fetchRefundAccount(long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QRefundAccountInfo(
                                        refundAccount.id,
                                        refundAccount.bankName,
                                        refundAccount.accountNumber,
                                        refundAccount.accountHolderName))
                        .from(refundAccount)
                        .where(userIdEq(userId), deleteYnEq())
                        .fetchOne());
    }

    @Override
    public Optional<RefundAccountInfo> fetchRefundAccountByPaymentId(long paymentId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QRefundAccountInfo(
                                        paymentSnapShotRefundAccount.bankName,
                                        paymentSnapShotRefundAccount.accountNumber,
                                        paymentSnapShotRefundAccount.accountHolderName))
                        .from(paymentSnapShotRefundAccount)
                        .where(paymentIdEq(paymentId), deleteYnEq())
                        .fetchOne());
    }

    @Override
    public Optional<RefundAccount> fetchRefundAccountEntity(long refundAccountId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(refundAccount)
                        .where(userIdEq(userId), refundAccountIdEq(refundAccountId))
                        .fetchOne());
    }

    private BooleanExpression refundAccountIdEq(long userId) {
        return refundAccount.id.eq(userId);
    }

    private BooleanExpression deleteYnEq() {
        return refundAccount.deleteYn.eq(Yn.N);
    }

    private BooleanExpression userIdEq(long userId) {
        return refundAccount.userId.eq(userId);
    }

    private BooleanExpression paymentIdEq(long paymentId) {
        return paymentSnapShotRefundAccount.paymentId.eq(paymentId);
    }
}
