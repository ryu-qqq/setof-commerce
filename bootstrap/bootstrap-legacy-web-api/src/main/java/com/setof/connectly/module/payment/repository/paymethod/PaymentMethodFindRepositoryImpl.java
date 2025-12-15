package com.setof.connectly.module.payment.repository.paymethod;

import static com.setof.connectly.module.payment.entity.QPaymentMethod.paymentMethod;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.payment.dto.paymethod.PayMethodResponse;
import com.setof.connectly.module.payment.dto.paymethod.QPayMethodResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentMethodFindRepositoryImpl implements PaymentMethodFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PayMethodResponse> fetchPayMethods() {
        return queryFactory
                .from(paymentMethod)
                .where(displayYnEq())
                .transform(
                        GroupBy.groupBy(paymentMethod.id)
                                .list(
                                        new QPayMethodResponse(
                                                paymentMethod.paymentMethodEnum,
                                                paymentMethod.paymentMethodMerchantKey)));
    }

    private BooleanExpression displayYnEq() {
        return paymentMethod.displayYn.eq(Yn.Y);
    }
}
