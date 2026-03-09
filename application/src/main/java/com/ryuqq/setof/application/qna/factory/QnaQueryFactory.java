package com.ryuqq.setof.application.qna.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.query.QnaSortKey;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import org.springframework.stereotype.Component;

/**
 * QnaQueryFactory - Q&A 조회 SearchParams → SearchCriteria 변환 Factory.
 *
 * <p>Application DTO(SearchParams)를 Domain SearchCriteria로 변환합니다. CommonVoFactory를 사용하여 Domain VO를
 * 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public QnaQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    /**
     * 상품 Q&A 조회용 검색 조건 생성 (오프셋 기반).
     *
     * @param params 상품 Q&A 조회 파라미터
     * @return ProductQnaSearchCriteria
     */
    public ProductQnaSearchCriteria createProductQnaCriteria(ProductQnaSearchParams params) {
        PageRequest pageRequest =
                commonVoFactory.createPageRequest(params.pageOrDefault(), params.sizeOrDefault());

        QueryContext<QnaSortKey> queryContext =
                commonVoFactory.createQueryContext(
                        QnaSortKey.defaultKey(), SortDirection.DESC, pageRequest);

        return ProductQnaSearchCriteria.of(ProductGroupId.of(params.productGroupId()), queryContext);
    }

    /**
     * 내 Q&A 조회용 검색 조건 생성 (커서 기반).
     *
     * @param params 내 Q&A 조회 파라미터
     * @return QnaSearchCriteria
     */
    public QnaSearchCriteria createMyQnaCriteria(MyQnaSearchParams params) {
        LegacyMemberId memberId =
                params.userId() != null ? LegacyMemberId.of(params.userId()) : null;

        CursorPageRequest<Long> pageRequest =
                params.lastQnaId() != null
                        ? commonVoFactory.createCursorPageRequest(
                                params.lastQnaId(), params.sizeOrDefault())
                        : commonVoFactory.createCursorPageRequest(params.sizeOrDefault());

        CursorQueryContext<QnaSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        QnaSortKey.defaultKey(), SortDirection.DESC, pageRequest);

        QnaType qnaType = QnaType.valueOf(params.qnaType());

        return QnaSearchCriteria.ofMyQnas(
                memberId, qnaType, params.startDate(), params.endDate(), queryContext);
    }
}
