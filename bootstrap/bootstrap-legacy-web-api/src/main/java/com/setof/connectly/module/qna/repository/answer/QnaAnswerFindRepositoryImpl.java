package com.setof.connectly.module.qna.repository.answer;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.qna.entity.QnaAnswer;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaWriterType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.setof.connectly.module.qna.entity.QQnaAnswer.qnaAnswer;

@Repository
@RequiredArgsConstructor
public class QnaAnswerFindRepositoryImpl implements QnaAnswerFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<QnaAnswer> fetchQnaAnswerEntity(long qnaAnswerId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qnaAnswer)
                        .where(qnaAnswerIdEq(qnaAnswerId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<QnaAnswer> fetchQnaAnswerOpenStatus(long qnaId) {
        return Optional.ofNullable(queryFactory.selectFrom(qnaAnswer)
                .where(qnaIdEq(qnaId), qnaWriterSellerEq(), qnaAnswerStatusOpen())
                .orderBy(qnaAnswer.id.desc())
                .fetchFirst());

    }


    private BooleanExpression qnaIdEq(long qnaId){
        return qnaAnswer.qnaId.eq(qnaId);
    }

    private BooleanExpression qnaWriterSellerEq(){
        return qnaAnswer.qnaWriterType.eq(QnaWriterType.SELLER);
    }

    private BooleanExpression qnaAnswerStatusOpen(){
        return qnaAnswer.qnaStatus.eq(QnaStatus.OPEN);
    }

    private BooleanExpression qnaAnswerIdEq(long qnaAnswerId){
        return qnaAnswer.id.eq(qnaAnswerId);
    }
}
