package com.setof.connectly.module.order.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.mapper.AbstractGeneralSliceMapper;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class OrderSliceMapperImpl extends AbstractGeneralSliceMapper<ReviewOrderProductDto>
        implements OrderSliceMapper {

    @Override
    public CustomSlice<ReviewOrderProductDto> toSlice(
            List<ReviewOrderProductDto> data, Pageable pageable, long totalElements) {
        data.forEach(ReviewOrderProductDto::setOption);

        return super.toSlice(data, pageable, totalElements);
    }
}
