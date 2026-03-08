package com.ryuqq.setof.application.cart.dto.response;

import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;

/**
 * 장바구니 목록 커서 기반 페이징 결과 DTO.
 *
 * @param content 장바구니 항목 목록
 * @param sliceMeta 슬라이스 메타 정보
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartSliceResult(
        List<CartItemResult> content, SliceMeta sliceMeta, long totalElements) {

    public static CartSliceResult of(
            List<CartItemResult> content, SliceMeta sliceMeta, long totalElements) {
        return new CartSliceResult(content, sliceMeta, totalElements);
    }

    public static CartSliceResult empty() {
        return new CartSliceResult(List.of(), SliceMeta.empty(), 0L);
    }

    public boolean hasNext() {
        return sliceMeta.hasNext();
    }
}
