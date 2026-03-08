package com.ryuqq.setof.application.productgroup.dto.response;

import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;

/**
 * ProductGroupSliceResult - 상품그룹 커서 슬라이스 응답 DTO.
 *
 * <p>목록 조회 (fetchProductGroups, search, brand/seller 필터)의 공통 응답입니다. V1 ApiMapper에서 레거시 CustomSlice
 * 포맷으로 변환합니다.
 *
 * @param content 상품그룹 썸네일 목록
 * @param sliceMeta 커서 페이징 메타 (hasNext, lastId, size 등)
 * @param totalElements 전체 건수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupSliceResult(
        List<ProductGroupThumbnailResult> content, SliceMeta sliceMeta, long totalElements) {

    public static ProductGroupSliceResult of(
            List<ProductGroupThumbnailResult> content, SliceMeta sliceMeta, long totalElements) {
        return new ProductGroupSliceResult(content, sliceMeta, totalElements);
    }

    public static ProductGroupSliceResult empty(int requestedSize) {
        return new ProductGroupSliceResult(
                List.of(), SliceMeta.withCursor((String) null, requestedSize, false, 0), 0L);
    }
}
