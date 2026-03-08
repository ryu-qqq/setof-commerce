package com.ryuqq.setof.application.wishlist.internal;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.manager.WishlistCommandManager;
import com.ryuqq.setof.application.wishlist.manager.WishlistReadManager;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * WishlistDeletionCoordinator - 찜 항목 삭제 Coordinator.
 *
 * <p>ReadManager → 도메인 상태 변경(delete) → CommandManager persist. 하이버네이트 더티체킹으로 상태가 반영됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistDeletionCoordinator {

    private final WishlistReadManager readManager;
    private final WishlistCommandManager commandManager;
    private final TimeProvider timeProvider;

    public WishlistDeletionCoordinator(
            WishlistReadManager readManager,
            WishlistCommandManager commandManager,
            TimeProvider timeProvider) {
        this.readManager = readManager;
        this.commandManager = commandManager;
        this.timeProvider = timeProvider;
    }

    @Transactional
    public void delete(DeleteWishlistItemCommand command) {
        Instant now = timeProvider.now();
        Optional<WishlistItem> wishlistItem =
                readManager.findByUserIdAndProductGroupId(
                        command.userId(), command.productGroupId());

        wishlistItem.ifPresent(
                item -> {
                    item.delete(now);
                    commandManager.persist(item);
                });
    }
}
