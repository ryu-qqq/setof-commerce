package com.setof.connectly.module.cart.service.query;

import com.setof.connectly.module.cart.dto.CartDeleteRequestDto;
import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import java.util.List;

public interface CartQueryService {

    List<CartDetails> insertOrUpdate(List<CartDetails> cartDetails);

    CartDetails updateCart(long cartId, CartDetails cartDetails);

    int delete(CartDeleteRequestDto requestDto);

    void delete(List<Long> productIds);

    void rollBack(List<Long> cartIds);
}
