package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.common.mapper.AbstractProductSliceMapper;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ProductSliceMapperImpl extends AbstractProductSliceMapper<ProductGroupThumbnail>
        implements ProductSliceMapper {

    @Override
    public CustomSlice<ProductGroupThumbnail> toSlice(
            List<ProductGroupThumbnail> data, Pageable pageable, long totalElements) {
        return super.toSlice(data, pageable, totalElements);
    }

    @Override
    public CustomSlice<ProductGroupThumbnail> toSlice(
            List<ProductGroupThumbnail> data,
            Pageable pageable,
            long totalElements,
            ItemFilter filter) {
        return super.toSlice(data, pageable, totalElements, filter);
    }
}
