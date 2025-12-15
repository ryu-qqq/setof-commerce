package com.setof.connectly.module.qna.repository.image.fetch;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.setof.connectly.module.qna.entity.QQnaImage.qnaImage;

@Repository
@RequiredArgsConstructor
public class QnaImageFindRepositoryImpl implements QnaImageFindRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QnaImage> fetchQnaImageEntities(QnaIssueType qnaIssueType, long qnaAnswerId){
        return queryFactory.selectFrom(qnaImage)
                .where(qnaIssueTypeEq(qnaIssueType), qnaAnswerIdEq(qnaAnswerId), qnaImage.deleteYn.eq(Yn.N))
                .fetch();
    }

    @Override
    public List<QnaImage> fetchQnaImageEntitiesByQnaId(long qnaId) {
        return queryFactory.selectFrom(qnaImage)
                .where(qnaIdEq(qnaId), qnaImage.deleteYn.eq(Yn.N))
                .fetch();
    }

    @Override
    public List<QnaImage> fetchQnaImageEntitiesByQnaId(List<Long> qnaIds) {
        return queryFactory.selectFrom(qnaImage)
                .where(qnaIdIn(qnaIds), qnaImage.deleteYn.eq(Yn.N))
                .fetch();
    }


    private BooleanExpression qnaIdEq(long qnaId){
        return qnaImage.qnaId.eq(qnaId);
    }

    private BooleanExpression qnaIdIn(List<Long> qnaIds){
        return qnaImage.qnaId.in(qnaIds);
    }


    private BooleanExpression qnaAnswerIdEq(long qnaAnswerId){
        return qnaImage.qnaAnswerId.eq(qnaAnswerId);
    }

    private BooleanExpression qnaIssueTypeEq(QnaIssueType qnaIssueType){
        if(qnaIssueType != null) return qnaImage.qnaIssueType.eq(qnaIssueType);
        else return null;
    }
}
