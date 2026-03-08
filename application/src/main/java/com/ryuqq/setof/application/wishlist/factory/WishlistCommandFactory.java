package com.ryuqq.setof.application.wishlist.factory;

import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import org.springframework.stereotype.Component;

/**
 * WishlistCommandFactory - 찜 항목 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistCommandFactory {

    public WishlistItem createNewItem(AddWishlistItemCommand command) {
        return WishlistItem.create(
                LegacyMemberId.of(command.userId()), ProductGroupId.of(command.productGroupId()));
    }
}
