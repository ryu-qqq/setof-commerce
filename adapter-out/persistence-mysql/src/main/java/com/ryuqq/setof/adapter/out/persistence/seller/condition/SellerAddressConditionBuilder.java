package com.ryuqq.setof.adapter.out.persistence.seller.condition;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerAddressJpaEntity.sellerAddressJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.seller.query.SellerAddressSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerAddressSearchField;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerAddressConditionBuilder - 셀러 주소 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerAddressConditionBuilder {

    /** 셀러 ID 일치 조건 */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? sellerAddressJpaEntity.sellerId.eq(sellerId) : null;
    }

    /** 셀러 ID 목록 포함 조건 */
    public BooleanExpression sellerIdIn(List<Long> sellerIds) {
        return sellerIds != null && !sellerIds.isEmpty()
                ? sellerAddressJpaEntity.sellerId.in(sellerIds)
                : null;
    }

    /** 주소 유형 일치 조건 */
    public BooleanExpression addressTypeEq(AddressType addressType) {
        return addressType != null
                ? sellerAddressJpaEntity.addressType.eq(addressType.name())
                : null;
    }

    /** 주소 유형 목록 포함 조건 */
    public BooleanExpression addressTypeIn(List<AddressType> addressTypes) {
        if (addressTypes == null || addressTypes.isEmpty()) {
            return null;
        }
        List<String> typeNames = addressTypes.stream().map(AddressType::name).toList();
        return sellerAddressJpaEntity.addressType.in(typeNames);
    }

    /** 기본 주소 조건 */
    public BooleanExpression defaultAddressEq(Boolean defaultAddress) {
        return defaultAddress != null
                ? sellerAddressJpaEntity.defaultAddress.eq(defaultAddress)
                : null;
    }

    /** 주소명 일치 조건 */
    public BooleanExpression addressNameEq(String addressName) {
        return addressName != null && !addressName.isBlank()
                ? sellerAddressJpaEntity.addressName.eq(addressName)
                : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return sellerAddressJpaEntity.deletedAt.isNull();
    }

    /** 검색 필드 기반 검색 조건. */
    public BooleanExpression searchFieldContains(
            SellerAddressSearchField searchField, String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        if (searchField == null) {
            return sellerAddressJpaEntity.addressName.containsIgnoreCase(searchWord);
        }
        return switch (searchField) {
            case ADDRESS_NAME -> sellerAddressJpaEntity.addressName.containsIgnoreCase(searchWord);
            case ADDRESS -> sellerAddressJpaEntity.address.containsIgnoreCase(searchWord);
        };
    }

    /** 검색 조건 (Criteria 기반). */
    public BooleanExpression searchCondition(SellerAddressSearchCriteria criteria) {
        if (!criteria.hasSearchCondition()) {
            return null;
        }
        return searchFieldContains(criteria.searchField(), criteria.searchWord());
    }
}
