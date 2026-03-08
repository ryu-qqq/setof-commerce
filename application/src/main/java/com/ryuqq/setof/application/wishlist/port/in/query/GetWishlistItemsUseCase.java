package com.ryuqq.setof.application.wishlist.port.in.query;

import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;

/**
 * 찜 목록 조회 UseCase.
 *
 * <p>레거시 GET /api/v1/user/my-favorites 기반. 커서 기반 페이지네이션으로 상품 정보 포함 복합 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetWishlistItemsUseCase {

    WishlistItemSliceResult execute(WishlistSearchParams params);
}
