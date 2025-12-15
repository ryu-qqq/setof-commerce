package com.setof.connectly.module.search.service;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.search.dto.SearchFilter;
import org.springframework.data.domain.Pageable;

public interface SearchFindService {
    CustomSlice<ProductGroupThumbnail> fetchSearchResults(SearchFilter filter, Pageable pageable);
}
