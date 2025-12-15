package com.setof.connectly.module.cart.mapper;

import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.common.dto.CustomSlice;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CartSliceMapper {
    CustomSlice<CartResponse> toSlice(List<CartResponse> carts, Pageable pageable, long totalCount);
}
