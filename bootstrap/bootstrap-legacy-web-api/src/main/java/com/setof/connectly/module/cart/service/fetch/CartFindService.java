package com.setof.connectly.module.cart.service.fetch;

import com.setof.connectly.module.cart.dto.CartCountDto;
import com.setof.connectly.module.cart.dto.CartFilter;
import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.entity.Cart;
import com.setof.connectly.module.common.dto.CustomSlice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CartFindService {

    List<Cart> fetchCartEntities(List<Long> cartIdList);

    List<CartResponse> fetchCarts(CartFilter filter, Pageable pageable);

    Cart fetchCartEntity(long cartId);

    Optional<Cart> fetchExistingCartEntityByProductId(long productId);

    List<Cart> fetchExistingCartEntityByProductIds(List<Long> productIds);

    CustomSlice<CartResponse> fetchCartList(CartFilter filter, Pageable pageable);

    CartCountDto fetchCartCountQuery(long userId);
}
