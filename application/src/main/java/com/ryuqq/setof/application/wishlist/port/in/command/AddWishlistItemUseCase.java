package com.ryuqq.setof.application.wishlist.port.in.command;

import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;

/**
 * 찜 항목 추가 UseCase.
 *
 * <p>레거시 POST /api/v1/user/my-favorite 기반.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface AddWishlistItemUseCase {

    Long execute(AddWishlistItemCommand command);
}
