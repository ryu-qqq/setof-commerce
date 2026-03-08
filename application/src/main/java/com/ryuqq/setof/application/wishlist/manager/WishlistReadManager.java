package com.ryuqq.setof.application.wishlist.manager;

import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.port.out.query.WishlistQueryPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * WishlistReadManager - 찜 항목 조회 Manager.
 *
 * <p>QueryPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistReadManager {

    private final WishlistQueryPort queryPort;

    public WishlistReadManager(WishlistQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Optional<WishlistItem> findByUserIdAndProductGroupId(Long userId, long productGroupId) {
        return queryPort.findByUserIdAndProductGroupId(userId, productGroupId);
    }

    @Transactional(readOnly = true)
    public List<WishlistItemResult> fetchSlice(WishlistSearchCriteria criteria) {
        return queryPort.fetchSlice(criteria);
    }

    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        return queryPort.countByUserId(userId);
    }
}
