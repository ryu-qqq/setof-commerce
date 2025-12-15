package com.setof.connectly.module.search.service;

import com.setof.connectly.module.common.dto.CustomSlice;
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
        return fetchSearchExactlyResults(filter, pageable);
    }

    private CustomSlice<ProductGroupThumbnail> fetchSearchExactlyResults(
            SearchFilter filter, Pageable pageable) {
        List<ProductGroupThumbnail> exactlyHits =
                searchRepository.fetchResults(filter, pageable.getPageSize());
        long totalCount = searchRepository.fetchSearchCountQuery(filter);
        return null;
        // return productSliceMapper.toSlice(searchMapper.toProductGroupThumbnail(exactlyHits),
        // pageable, totalCount, filter);
    }
}
