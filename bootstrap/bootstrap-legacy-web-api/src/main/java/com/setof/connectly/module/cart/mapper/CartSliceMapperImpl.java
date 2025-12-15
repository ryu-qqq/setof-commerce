package com.setof.connectly.module.cart.mapper;

import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.mapper.AbstractGeneralSliceMapper;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CartSliceMapperImpl extends AbstractGeneralSliceMapper<CartResponse>
        implements CartSliceMapper {

    @Override
    public CustomSlice<CartResponse> toSlice(
            List<CartResponse> data, Pageable pageable, long totalCount) {
        return super.toSlice(data, pageable, totalCount);
    }
}
