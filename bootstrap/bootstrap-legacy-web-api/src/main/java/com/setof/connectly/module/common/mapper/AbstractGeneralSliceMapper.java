package com.setof.connectly.module.common.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.utils.SliceUtils;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGeneralSliceMapper<T extends LastDomainIdProvider>
        implements GeneralSliceMapper<T> {

    @Override
    public CustomSlice<T> toSlice(List<T> data, Pageable pageable) {
        Slice<T> slice = SliceUtils.toSlice(data, pageable);

        Long lastDomainId = null;

        if (!slice.isEmpty()) {
            List<T> content = slice.getContent();
            T t = content.get(content.size() - 1);
            lastDomainId = t.getId();
        }

        return buildCustomSlice(slice, null, lastDomainId);
    }

    @Override
    public CustomSlice<T> toSlice(List<T> data, Pageable pageable, long totalElements) {
        Slice<T> slice = SliceUtils.toSlice(data, pageable);
        Long lastDomainId = null;

        if (!slice.isEmpty()) {
            List<T> content = slice.getContent();
            T t = content.get(content.size() - 1);
            lastDomainId = t.getId();
        }

        return buildCustomSlice(slice, totalElements, lastDomainId);
    }

    private CustomSlice<T> buildCustomSlice(Slice<T> slice, Long totalElements, Long lastDomainId) {
        return CustomSlice.<T>builder()
                .content(slice.getContent())
                .last(slice.isLast())
                .first(slice.isFirst())
                .number(slice.getNumber())
                .sort(slice.getSort())
                .size(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .empty(slice.isEmpty())
                .lastDomainId(lastDomainId)
                .totalElements(totalElements)
                .build();
    }
}
