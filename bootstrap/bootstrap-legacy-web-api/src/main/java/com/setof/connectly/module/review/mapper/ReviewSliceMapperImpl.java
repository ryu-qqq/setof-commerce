package com.setof.connectly.module.review.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.mapper.AbstractGeneralSliceMapper;
import com.setof.connectly.module.review.dto.ReviewDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ReviewSliceMapperImpl extends AbstractGeneralSliceMapper<ReviewDto>
        implements ReviewSliceMapper {

    @Override
    public CustomSlice<ReviewDto> toSlice(List<ReviewDto> data, Pageable pageable) {
        return super.toSlice(data, pageable);
    }
}
