package com.ryuqq.setof.application.wishlist.dto.response;

import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;

/**
 * 찜 목록 커서 기반 페이징 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param content 찜 항목 목록
 * @param sliceMeta 슬라이스 메타 정보
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WishlistItemSliceResult(
        List<WishlistItemResult> content, SliceMeta sliceMeta, long totalElements) {

    public static WishlistItemSliceResult of(
            List<WishlistItemResult> content, SliceMeta sliceMeta, long totalElements) {
        return new WishlistItemSliceResult(content, sliceMeta, totalElements);
    }

    public static WishlistItemSliceResult empty() {
        return new WishlistItemSliceResult(List.of(), SliceMeta.empty(), 0L);
    }

    public boolean hasNext() {
        return sliceMeta.hasNext();
    }
}
