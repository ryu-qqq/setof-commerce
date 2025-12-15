package com.setof.connectly.module.common.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.filter.LastDomainIdFilter;
import com.setof.connectly.module.utils.SliceUtils;
import com.setof.connectly.module.utils.SortUtils;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractProductSliceMapper<T extends CursorValueProvider>
        implements ProductSliceMapper<T> {

    @Override
    public CustomSlice<T> toSlice(List<T> data, Pageable pageable, long totalElements) {
        Slice<T> slice = SliceUtils.toSlice(data, pageable);
        return buildCustomSlice(slice, totalElements, null, null);
    }

    @Override
    public CustomSlice<T> toSlice(
            List<T> data, Pageable pageable, long totalElements, LastDomainIdFilter filter) {
        if (data.isEmpty()) {
            return buildCustomSlice(SliceUtils.emptySlice(pageable), totalElements, null, null);
        }

        Long lastDomainId = null;
        String cursorValue = null;
        Slice<T> slice = SliceUtils.toSlice(data, pageable);

        T lastItem = data.get(data.size() - 1);
        lastDomainId = lastItem.getId();
        cursorValue = SortUtils.setCursorValue(lastItem, filter.getOrderType());

        Comparator<CursorValueProvider> comparator =
                SortUtils.getComparatorBasedOnOrderType(filter.getOrderType());
        if (comparator != null) {
            List<T> sortedData = data.stream().sorted(comparator).collect(Collectors.toList());
            lastItem = sortedData.get(sortedData.size() - 1);
            lastDomainId = lastItem.getId();
            cursorValue = SortUtils.setCursorValue(lastItem, filter.getOrderType());
            slice = SliceUtils.toSlice(sortedData, pageable);
        }

        return buildCustomSlice(slice, totalElements, lastDomainId, cursorValue);
    }

    private CustomSlice<T> buildCustomSlice(
            Slice<T> slice, Long totalElements, Long lastDomainId, String cursorValue) {
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
                .cursorValue(cursorValue)
                .totalElements(totalElements)
                .build();
    }
}
