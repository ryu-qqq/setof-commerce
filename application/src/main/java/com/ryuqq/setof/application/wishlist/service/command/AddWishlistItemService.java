package com.ryuqq.setof.application.wishlist.service.command;

import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.internal.WishlistRegistrationCoordinator;
import com.ryuqq.setof.application.wishlist.port.in.command.AddWishlistItemUseCase;
import org.springframework.stereotype.Service;

/**
 * AddWishlistItemService - 찜 항목 추가 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class AddWishlistItemService implements AddWishlistItemUseCase {

    private final WishlistRegistrationCoordinator coordinator;

    public AddWishlistItemService(WishlistRegistrationCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public Long execute(AddWishlistItemCommand command) {
        return coordinator.register(command);
    }
}
