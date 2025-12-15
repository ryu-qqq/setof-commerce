package com.setof.connectly.module.payment.repository.vbank;

import static com.setof.connectly.module.common.entity.QCommonCode.commonCode;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.payment.dto.refund.BankResponse;
import com.setof.connectly.module.payment.dto.refund.QBankResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VBankFindRepositoryImpl implements VBankFindRepository {

    private final JPAQueryFactory queryFactory;

    public List<BankResponse> fetchVBankAccounts() {
        return queryFactory
                .from(commonCode)
                .where(codeGroupIdVBank())
                .transform(
                        GroupBy.groupBy(commonCode.id)
                                .list(
                                        new QBankResponse(
                                                commonCode.codeDetail,
                                                commonCode.codeDetailDisplayName)));
    }

    @Override
    public List<BankResponse> fetchRefundBanks() {
        return queryFactory
                .from(commonCode)
                .where(codeGroupIdRefundVBank())
                .transform(
                        GroupBy.groupBy(commonCode.id)
                                .list(
                                        new QBankResponse(
                                                commonCode.codeDetail,
                                                commonCode.codeDetailDisplayName)));
    }

    private BooleanExpression codeGroupIdVBank() {
        return commonCode.codeGroupId.eq(10L);
    }

    private BooleanExpression codeGroupIdRefundVBank() {
        return commonCode.codeGroupId.eq(12L);
    }
}
