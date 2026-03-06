package com.ryuqq.setof.application.seller.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

public record SellerSearchParams(
        Boolean active, String searchField, String searchWord, CommonSearchParams searchParams) {
    public static SellerSearchParams of(
            Boolean active,
            String searchField,
            String searchWord,
            CommonSearchParams searchParams) {
        return new SellerSearchParams(active, searchField, searchWord, searchParams);
    }

    public int page() {
        return searchParams.page();
    }

    public int size() {
        return searchParams.size();
    }

    public String sortKey() {
        return searchParams.sortKey();
    }

    public String sortDirection() {
        return searchParams.sortDirection();
    }
}
