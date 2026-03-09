package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import java.util.Map;

/**
 * ComponentFixedProductQueryPort - FIXED 상품 조회 출력 포트.
 *
 * <p>component_target → component_item 경로로 고정(FIXED) 배치된 상품을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ComponentFixedProductQueryPort {

    /**
     * 컴포넌트별 FIXED 상품 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return componentId → 상품 목록 맵
     */
    Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProducts(List<Long> componentIds);

    /**
     * 탭별 FIXED 상품 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return tabId → 상품 목록 맵
     */
    Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProductsByTab(List<Long> componentIds);
}
