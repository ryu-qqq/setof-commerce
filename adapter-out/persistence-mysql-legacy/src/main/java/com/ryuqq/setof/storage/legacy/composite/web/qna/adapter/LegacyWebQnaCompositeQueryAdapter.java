package com.ryuqq.setof.storage.legacy.composite.web.qna.adapter;

import com.ryuqq.setof.application.legacy.qna.dto.response.LegacyQnaResult;
import com.ryuqq.setof.domain.legacy.qna.dto.query.LegacyQnaSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.qna.dto.LegacyWebQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.qna.mapper.LegacyWebQnaMapper;
import com.ryuqq.setof.storage.legacy.composite.web.qna.repository.LegacyWebQnaCompositeQueryDslRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaCompositeQueryAdapter - 레거시 Web Q&A Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyQnaCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaCompositeQueryAdapter {

    private final LegacyWebQnaCompositeQueryDslRepository repository;
    private final LegacyWebQnaMapper mapper;

    public LegacyWebQnaCompositeQueryAdapter(
            LegacyWebQnaCompositeQueryDslRepository repository, LegacyWebQnaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 Q&A 목록 조회 (fetchProductQnas).
     *
     * <p>2단계 조회: ID 목록 -> 상세 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @param pageable 페이징 정보
     * @return Q&A 목록
     */
    public List<LegacyQnaResult> fetchProductQnas(Long productGroupId, Pageable pageable) {
        List<Long> qnaIds = repository.fetchQnaProductIds(productGroupId, pageable);
        List<LegacyWebQnaQueryDto> dtos = repository.fetchQnas(qnaIds);
        return mapper.toResults(dtos);
    }

    /**
     * 상품그룹 Q&A 개수 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return Q&A 개수
     */
    public long countProductQnas(Long productGroupId) {
        return repository.countQnasByProductGroupId(productGroupId);
    }

    /**
     * 내 Q&A 목록 조회 (fetchMyQnas).
     *
     * <p>2단계 조회: ID 목록 -> 상세 조회.
     *
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return Q&A 목록
     */
    public List<LegacyQnaResult> fetchMyQnas(
            LegacyQnaSearchCondition condition, Pageable pageable) {
        List<Long> qnaIds = repository.fetchMyQnaIds(condition, pageable);
        if (condition.isProductQna()) {
            List<LegacyWebQnaQueryDto> dtos = repository.fetchMyProductQnas(qnaIds);
            return mapper.toResults(dtos);
        }
        // TODO: ORDER Q&A는 별도 구현 필요
        return List.of();
    }
}
