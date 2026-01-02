package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemSelectedCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import java.util.UUID;

/**
 * 장바구니 아이템 선택 상태 변경 UseCase
 *
 * <p>장바구니 아이템의 선택 상태를 변경합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateCartItemSelectedUseCase {

    /**
     * 아이템 선택 상태 변경
     *
     * @param command 선택 상태 변경 Command
     * @return 업데이트된 장바구니 응답
     */
    CartResponse updateSelected(UpdateCartItemSelectedCommand command);

    /**
     * 전체 아이템 선택/해제
     *
     * @param memberId 회원 ID (UUID)
     * @param selected 선택 상태
     * @return 업데이트된 장바구니 응답
     */
    CartResponse updateAllSelected(UUID memberId, boolean selected);
}
