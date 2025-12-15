package com.setof.connectly.module.cart.mapper;

import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.entity.Cart;
import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import java.util.List;

public interface CartMapper {

    Cart toEntity(long userId, CartDetails cartDetails);

    List<CartResponse> toCartResponses(
            List<CartResponse> carts, List<ProductCategoryDto> productCategories);
}
