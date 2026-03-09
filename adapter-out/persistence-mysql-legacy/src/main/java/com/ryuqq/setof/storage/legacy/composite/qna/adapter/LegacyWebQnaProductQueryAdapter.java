package com.ryuqq.setof.storage.legacy.composite.qna.adapter;

import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaProductQueryPort;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.mapper.LegacyWebQnaProductMapper;
import com.ryuqq.setof.storage.legacy.composite.qna.repository.LegacyWebQnaProductQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaProductQueryAdapter - 상품 Q&A 조회 Adapter.
 *
 * <p>QnaProductQueryPort 구현체입니다. Adapter에서 criteria의 primitive 값을 추출하여 Repository에 직접 전달합니다. 2-step
 * 쿼리(ID 조회 → 상세 조회)를 Adapter 내부에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaProductQueryAdapter implements QnaProductQueryPort {

    private final LegacyWebQnaProductQueryDslRepository repository;
    private final LegacyWebQnaProductMapper mapper;

    public LegacyWebQnaProductQueryAdapter(
            LegacyWebQnaProductQueryDslRepository repository, LegacyWebQnaProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<QnaWithAnswersResult> fetchProductQnas(ProductQnaSearchCriteria criteria) {
        List<Long> qnaIds =
                repository.fetchProductQnaIds(
                        criteria.productGroupIdValue(), criteria.offset(), criteria.size());

        if (qnaIds.isEmpty()) {
            return List.of();
        }

        List<LegacyWebQnaQueryDto> dtos = repository.fetchProductQnasByIds(qnaIds);
        return mapper.toQnaWithAnswersResults(dtos);
    }

    @Override
    public long countProductQnas(ProductQnaSearchCriteria criteria) {
        return repository.countProductQnas(criteria.productGroupIdValue());
    }

    @Override
    public Optional<QnaWithAnswersResult> findProductQnaById(long qnaId) {
        return repository.fetchProductQnasByIds(List.of(qnaId)).stream()
                .map(mapper::toQnaWithAnswersResult)
                .findFirst();
    }
}
