package com.ryuqq.setof.application.wishlist.service.command;

import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.internal.WishlistDeletionCoordinator;
import com.ryuqq.setof.application.wishlist.port.in.command.DeleteWishlistItemUseCase;
import org.springframework.stereotype.Service;

/**
 * DeleteWishlistItemService - 찜 항목 삭제 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class DeleteWishlistItemService implements DeleteWishlistItemUseCase {

    private final WishlistDeletionCoordinator coordinator;

    public DeleteWishlistItemService(WishlistDeletionCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(DeleteWishlistItemCommand command) {
        coordinator.delete(command);
    }
}
