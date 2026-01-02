package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.RemoveCartItemCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import java.util.UUID;

/**
 * 장바구니 아이템 삭제 UseCase
 *
 * <p>장바구니에서 아이템을 삭제합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RemoveCartItemUseCase {

    /**
     * 아이템 삭제
     *
     * @param command 아이템 삭제 Command
     * @return 업데이트된 장바구니 응답
     */
    CartResponse removeItems(RemoveCartItemCommand command);

    /**
     * 선택된 아이템만 삭제
     *
     * @param memberId 회원 ID (UUID)
     * @return 업데이트된 장바구니 응답
     */
    CartResponse removeSelectedItems(UUID memberId);
}
