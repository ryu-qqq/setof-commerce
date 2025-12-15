package com.setof.connectly.module.cart.repository.fetch;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.cart.dto.CartFilter;
import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.entity.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CartFindRepository {
    Optional<Cart> fetchCartEntity(long cartId, long userId);

    Optional<Cart> fetchExistingCartEntityByProductId(long productId, long userId);

    List<Cart> fetchExistingCartEntityByProductIds(List<Long> productId, long userId);

    List<Cart> fetchCartEntities(List<Long> cartIdList, long userId);

    List<CartResponse> fetchCartList(long userId, CartFilter filter, Pageable pageable);

    JPAQuery<Long> fetchCartCountQuery(long userId);
}
