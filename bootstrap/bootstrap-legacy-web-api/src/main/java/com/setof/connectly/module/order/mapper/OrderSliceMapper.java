package com.setof.connectly.module.order.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderSliceMapper {

    CustomSlice<ReviewOrderProductDto> toSlice(
            List<ReviewOrderProductDto> orderProducts, Pageable pageable, long totalElements);
}
