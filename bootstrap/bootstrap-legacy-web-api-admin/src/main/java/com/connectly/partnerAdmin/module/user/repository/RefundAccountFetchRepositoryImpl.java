package com.connectly.partnerAdmin.module.user.repository;


import com.connectly.partnerAdmin.module.user.dto.QRefundAccountInfo;
import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.connectly.partnerAdmin.module.user.entity.QRefundAccount.refundAccount;


@Repository
@RequiredArgsConstructor
public class RefundAccountFetchRepositoryImpl implements RefundAccountFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<RefundAccountInfo> fetchRefundAccount(long userId){
        return Optional.ofNullable(
                queryFactory.select(
                            new QRefundAccountInfo(
                                    refundAccount.id,
                                    refundAccount.bankName,
                                    refundAccount.accountNumber,
                                    refundAccount.accountHolderName
                            )
                        )
                        .from(refundAccount)
                        .where(userIdEq(userId))
                        .fetchOne()
        );
    }



    private BooleanExpression userIdEq(long userId){
        return refundAccount.users.id.eq(userId);
    }

}
