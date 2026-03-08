package com.ryuqq.setof.application.wishlist.assembler;

import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * WishlistAssembler - 찜 항목 Result DTO 조립.
 *
 * <p>Manager에서 조회한 데이터를 조합하여 최종 응답 객체를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistAssembler {

    public WishlistItemSliceResult toSliceResult(
            List<WishlistItemResult> items, int requestedSize, long totalElements) {
        boolean hasNext = items.size() > requestedSize;
        List<WishlistItemResult> content = hasNext ? items.subList(0, requestedSize) : items;
        Long lastId = content.isEmpty() ? null : content.get(content.size() - 1).userFavoriteId();
        SliceMeta sliceMeta = SliceMeta.withCursor(lastId, requestedSize, hasNext, content.size());
        return WishlistItemSliceResult.of(content, sliceMeta, totalElements);
    }
}
