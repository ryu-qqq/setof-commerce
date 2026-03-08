package com.ryuqq.setof.application.productgroup.dto.composite;

import java.util.List;

/**
 * ProductGroupListBundle - 상품그룹 목록 조회 번들 DTO.
 *
 * <p>ReadFacade에서 조회한 썸네일 결과와 전체 건수를 묶어 Service로 전달합니다. Service는 이 번들을 Assembler에 넘겨 최종 결과를 조립합니다.
 *
 * @param thumbnails 썸네일 목록 (pageSize + 1개 조회)
 * @param totalElements 전체 건수
 * @param orderType 정렬 타입 (커서 값 결정에 사용)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupListBundle(
        List<ProductGroupThumbnailCompositeResult> thumbnails,
        long totalElements,
        String orderType) {}
