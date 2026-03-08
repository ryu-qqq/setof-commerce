package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;

/**
 * 장바구니 항목 추가 UseCase.
 *
 * <p>레거시 POST /api/v1/cart 기반. Upsert 패턴: 동일 productId면 수량 업데이트, 없으면 INSERT.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface AddCartItemUseCase {

    List<CartItem> execute(AddCartItemCommand command);
}
