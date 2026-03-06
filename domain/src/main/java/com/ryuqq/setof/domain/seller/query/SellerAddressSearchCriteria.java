package com.ryuqq.setof.domain.seller.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import java.util.List;

/**
 * SellerAddress 검색 조건 Criteria.
 *
 * @param sellerIds 셀러 ID 목록 필터
 * @param addressTypes 주소 유형 필터
 * @param defaultAddress 기본주소 필터 (null이면 전체)
 * @param searchField 검색 필드
 * @param searchWord 검색어
 * @param queryContext 정렬 및 페이징 정보
 */
public record SellerAddressSearchCriteria(
        List<Long> sellerIds,
        List<AddressType> addressTypes,
        Boolean defaultAddress,
        SellerAddressSearchField searchField,
        String searchWord,
        QueryContext<SellerAddressSortKey> queryContext) {

    public static SellerAddressSearchCriteria of(
            List<Long> sellerIds,
            List<AddressType> addressTypes,
            Boolean defaultAddress,
            SellerAddressSearchField searchField,
            String searchWord,
            QueryContext<SellerAddressSortKey> queryContext) {
        return new SellerAddressSearchCriteria(
                sellerIds, addressTypes, defaultAddress, searchField, searchWord, queryContext);
    }

    public boolean hasSellerIds() {
        return sellerIds != null && !sellerIds.isEmpty();
    }

    public boolean hasAddressTypes() {
        return addressTypes != null && !addressTypes.isEmpty();
    }

    public boolean hasSearchCondition() {
        return searchWord != null && !searchWord.isBlank();
    }

    public boolean hasSearchField() {
        return searchField != null;
    }

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }

    public int page() {
        return queryContext.page();
    }
}
