package com.setof.connectly.module.review.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.review.dto.ReviewDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewSliceMapper {

    CustomSlice<ReviewDto> toSlice(
            List<ReviewDto> myReviews, Pageable pageable, long totalElements);
}
