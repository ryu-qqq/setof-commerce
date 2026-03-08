package com.ryuqq.setof.application.wishlist.port.out.query;

import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import java.util.List;
import java.util.Optional;

/**
 * WishlistQueryPort - 찜 항목 조회 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface WishlistQueryPort {

    Optional<WishlistItem> findByUserIdAndProductGroupId(Long userId, long productGroupId);

    List<WishlistItem> findAllByUserId(Long userId);

    List<WishlistItemResult> fetchSlice(WishlistSearchCriteria criteria);

    long countByUserId(Long userId);
}
