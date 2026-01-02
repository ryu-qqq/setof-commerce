package com.connectly.partnerAdmin.module.qna.repository;

import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaWriterType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.connectly.partnerAdmin.module.qna.entity.QQna.qna;
import static com.connectly.partnerAdmin.module.qna.entity.QQnaAnswer.qnaAnswer;

@RequiredArgsConstructor
@Repository
public class QnaAnswerFetchRepositoryImpl implements QnaAnswerFetchRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<QnaAnswer> fetchQnaAnswerEntity(long qnaAnswerId, Optional<Long> sellerIdOpt) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qnaAnswer)
                        .innerJoin(qnaAnswer.qna, qna)
                        .where(qnaAnswerIdEq(qnaAnswerId), sellerIdEq(sellerIdOpt))
                        .fetchOne()
        );
    }

    @Override
    public Optional<QnaAnswer> fetchQnaAnswerOpenStatus(long qnaId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qnaAnswer)
                        .innerJoin(qnaAnswer.qna, qna)
                        .where(qnaIdEq(qnaId), qnaWriterCustomerEq(), qnaAnswerStatusOpen())
                        .orderBy(qnaAnswer.id.desc())
                        .fetchFirst());

    }

    private BooleanExpression qnaIdEq(long qnaId){
        return qnaAnswer.qna.id.eq(qnaId);
    }

    private BooleanExpression qnaWriterCustomerEq(){
        return qnaAnswer.qnaWriterType.eq(QnaWriterType.CUSTOMER);
    }

    private BooleanExpression qnaAnswerStatusOpen(){
        return qnaAnswer.qnaStatus.eq(QnaStatus.OPEN);
    }

    private BooleanExpression qnaAnswerIdEq(long qnaAnswerId){
        return qnaAnswer.id.eq(qnaAnswerId);
    }

    private BooleanExpression sellerIdEq(Optional<Long> sellerIdOpt){
        return sellerIdOpt.map(qna.sellerId::eq).orElse(null);
    }
}
