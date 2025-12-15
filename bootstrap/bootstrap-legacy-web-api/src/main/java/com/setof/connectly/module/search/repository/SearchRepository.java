package com.setof.connectly.module.search.repository;

import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.search.dto.SearchFilter;
import java.util.List;

public interface SearchRepository {
    List<ProductGroupThumbnail> fetchResults(SearchFilter searchFilter, int size);

    long fetchSearchCountQuery(SearchFilter filter);
}
