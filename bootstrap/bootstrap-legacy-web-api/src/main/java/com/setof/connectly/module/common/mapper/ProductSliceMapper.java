package com.setof.connectly.module.common.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.filter.LastDomainIdFilter;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductSliceMapper<T extends CursorValueProvider> {

    CustomSlice<T> toSlice(List<T> data, Pageable pageable, long totalElements);

    CustomSlice<T> toSlice(
            List<T> data, Pageable pageable, long totalElements, LastDomainIdFilter filter);
}
