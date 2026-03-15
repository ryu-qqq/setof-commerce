package com.ryuqq.setof.application.productgroup.dto.composite;

import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.List;

/**
 * ProductGroupListBundle - 상품그룹 목록 조회 번들 DTO.
 *
 * <p>ReadFacade에서 조회한 목록 결과와 전체 건수를 묶어 Service로 전달합니다. Service는 이 번들을 Assembler에 넘겨 최종 결과를 조립합니다.
 *
 * @param results 상품그룹 목록 Composite 결과
 * @param totalElements 전체 건수
 * @param sortKey 정렬 키 (커서 값 결정에 사용, nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupListBundle(
        List<ProductGroupListCompositeResult> results,
        long totalElements,
        ProductGroupSortKey sortKey) {

    public ProductGroupListBundle {
        results = results != null ? List.copyOf(results) : List.of();
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public int size() {
        return results.size();
    }
}
