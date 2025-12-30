package com.ryuqq.setof.application.cart.port.in.query;

import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import java.util.UUID;

/**
 * 장바구니 조회 UseCase
 *
 * <p>장바구니를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCartUseCase {

    /**
     * 회원의 장바구니 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return 장바구니 응답
     */
    CartResponse getCart(UUID memberId);

    /**
     * 장바구니 아이템 개수 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return 아이템 개수
     */
    int getItemCount(UUID memberId);
}
