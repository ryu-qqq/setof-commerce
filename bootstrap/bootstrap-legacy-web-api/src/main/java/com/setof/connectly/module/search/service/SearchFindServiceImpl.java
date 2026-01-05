package com.setof.connectly.module.search.service;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.enums.component.OrderType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.mapper.ProductSliceMapper;
import com.setof.connectly.module.search.dto.SearchFilter;
import com.setof.connectly.module.search.repository.SearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFindServiceImpl implements SearchFindService {

    private final ProductSliceMapper productSliceMapper;
    private final SearchRepository searchRepository;

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchSearchResults(
            SearchFilter filter, Pageable pageable) {
        // 검색은 기본적으로 score(추천순) 기반 정렬
        if (filter.getOrderType() == null) {
            filter.setOrderType(OrderType.RECOMMEND);
        }
        return fetchSearchExactlyResults(filter, pageable);
    }

    private CustomSlice<ProductGroupThumbnail> fetchSearchExactlyResults(
            SearchFilter filter, Pageable pageable) {
        // SliceUtils.toSlice()가 hasNext 판단을 위해 pageSize + 1개 필요
        List<ProductGroupThumbnail> exactlyHits =
                searchRepository.fetchResults(filter, pageable.getPageSize() + 1);
        long totalCount = searchRepository.fetchSearchCountQuery(filter);
        return productSliceMapper.toSlice(exactlyHits, pageable, totalCount, filter);
    }
}
