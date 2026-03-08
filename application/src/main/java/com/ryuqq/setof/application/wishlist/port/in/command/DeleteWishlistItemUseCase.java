package com.ryuqq.setof.application.wishlist.port.in.command;

import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;

/**
 * 찜 항목 삭제 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DeleteWishlistItemUseCase {

    void execute(DeleteWishlistItemCommand command);
}
