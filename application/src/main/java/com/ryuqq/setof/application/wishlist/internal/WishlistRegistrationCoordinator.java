package com.ryuqq.setof.application.wishlist.internal;

import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.factory.WishlistCommandFactory;
import com.ryuqq.setof.application.wishlist.manager.WishlistCommandManager;
import com.ryuqq.setof.application.wishlist.manager.WishlistReadManager;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * WishlistRegistrationCoordinator - 찜 항목 등록 Coordinator.
 *
 * <p>기존 찜 여부 확인 후 없으면 Factory로 도메인 객체 생성 → CommandManager로 persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistRegistrationCoordinator {

    private final WishlistReadManager readManager;
    private final WishlistCommandFactory factory;
    private final WishlistCommandManager commandManager;

    public WishlistRegistrationCoordinator(
            WishlistReadManager readManager,
            WishlistCommandFactory factory,
            WishlistCommandManager commandManager) {
        this.readManager = readManager;
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Transactional
    public Long register(AddWishlistItemCommand command) {
        Optional<WishlistItem> existing =
                readManager.findByUserIdAndProductGroupId(
                        command.userId(), command.productGroupId());

        if (existing.isPresent()) {
            return existing.get().idValue();
        }

        WishlistItem newItem = factory.createNewItem(command);
        return commandManager.persist(newItem);
    }
}
