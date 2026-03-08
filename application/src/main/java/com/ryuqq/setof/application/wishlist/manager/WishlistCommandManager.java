package com.ryuqq.setof.application.wishlist.manager;

import com.ryuqq.setof.application.wishlist.port.out.command.WishlistCommandPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import org.springframework.stereotype.Component;

/**
 * WishlistCommandManager - 찜 항목 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistCommandManager {

    private final WishlistCommandPort commandPort;

    public WishlistCommandManager(WishlistCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(WishlistItem wishlistItem) {
        return commandPort.persist(wishlistItem);
    }
}
