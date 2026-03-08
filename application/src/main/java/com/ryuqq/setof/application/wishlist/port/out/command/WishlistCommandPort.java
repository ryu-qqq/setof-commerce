package com.ryuqq.setof.application.wishlist.port.out.command;

import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;

/**
 * WishlistCommandPort - 찜 항목 명령 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다. 도메인 객체 상태에 따라 persist 시 더티체킹으로 반영됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface WishlistCommandPort {

    Long persist(WishlistItem wishlistItem);
}
