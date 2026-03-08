package com.ryuqq.setof.storage.legacy.composite.web.qna.adapter;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaMyProductQueryPort;
import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.web.qna.dto.LegacyWebMyQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.qna.mapper.LegacyWebQnaCompositeMapper;
import com.ryuqq.setof.storage.legacy.composite.web.qna.repository.LegacyWebQnaCompositeQueryDslRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaCompositeQueryAdapter - PRODUCT 타입 내 Q&A 복합 조회 Adapter.
 *
 * <p>QnaMyProductQueryPort 구현체입니다. 상품 정보(상품그룹, 브랜드, 이미지) JOIN이 필요한 복합 조회를 처리합니다. Adapter에서
 * criteria의 primitive 값을 추출하여 Repository에 직접 전달합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaCompositeQueryAdapter implements QnaMyProductQueryPort {

    private final LegacyWebQnaCompositeQueryDslRepository repository;
    private final LegacyWebQnaCompositeMapper mapper;

    public LegacyWebQnaCompositeQueryAdapter(
            LegacyWebQnaCompositeQueryDslRepository repository,
            LegacyWebQnaCompositeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<MyQnaResult> fetchMyProductQnas(QnaSearchCriteria criteria) {
        DateRange dateRange = criteria.dateRange();
        LocalDate startDate = dateRange != null ? dateRange.startDate() : null;
        LocalDate endDate = dateRange != null ? dateRange.endDate() : null;

        List<Long> qnaIds =
                repository.fetchMyQnaIds(
                        criteria.memberIdValue(),
                        criteria.cursor(),
                        criteria.qnaTypeValue(),
                        startDate,
                        endDate,
                        criteria.fetchSize());

        if (qnaIds.isEmpty()) {
            return List.of();
        }

        List<LegacyWebMyQnaQueryDto> dtos = repository.fetchMyProductQnasByIds(qnaIds);
        return mapper.toMyQnaResults(dtos);
    }
}
