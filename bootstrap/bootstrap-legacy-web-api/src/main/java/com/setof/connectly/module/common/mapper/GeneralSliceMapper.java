package com.setof.connectly.module.common.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GeneralSliceMapper<T extends LastDomainIdProvider> {

    CustomSlice<T> toSlice(List<T> data, Pageable pageable);

    CustomSlice<T> toSlice(List<T> data, Pageable pageable, long totalElements);
}
