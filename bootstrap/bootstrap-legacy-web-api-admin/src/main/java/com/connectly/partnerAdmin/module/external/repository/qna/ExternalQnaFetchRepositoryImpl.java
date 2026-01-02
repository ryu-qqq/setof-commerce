package com.connectly.partnerAdmin.module.external.repository.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;
import com.connectly.partnerAdmin.module.external.dto.qna.QExternalQnaMappingDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.connectly.partnerAdmin.module.external.entity.QExternalQna.externalQna;

@RequiredArgsConstructor
@Repository
public class ExternalQnaFetchRepositoryImpl implements ExternalQnaFetchRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ExternalQnaMappingDto> fetchHasExternalQna(long qnaId){
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QExternalQnaMappingDto(
                                        externalQna.externalIdx,
                                        externalQna.siteId
                                )
                        )
                        .from(externalQna)
                        .where(qnaIdEq(qnaId))
                        .fetchOne()
        );
    }


    private BooleanExpression qnaIdEq(long qnaId){
        return externalQna.qnaId.eq(qnaId);
    }
}
