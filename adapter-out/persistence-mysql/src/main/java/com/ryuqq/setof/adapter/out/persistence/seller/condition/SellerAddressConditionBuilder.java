package com.ryuqq.setof.adapter.out.persistence.seller.condition;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerAddressJpaEntity.sellerAddressJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * SellerAddressConditionBuilder - 셀러 주소 QueryDSL 조건 빌더.
 *
 * <p>BooleanExpression 조건을 재사용 가능한 형태로 제공합니다.
 *
 * <p>PER-CND-001: BooleanExpression은 ConditionBuilder로 분리.
 */
@Component
public class SellerAddressConditionBuilder {

    /**
     * ID 일치 조건.
     *
     * @param id 주소 ID
     * @return ID 일치 조건
     */
    public BooleanExpression idEq(Long id) {
        return id != null ? sellerAddressJpaEntity.id.eq(id) : null;
    }

    /**
     * 셀러 ID 일치 조건.
     *
     * @param sellerId 셀러 ID
     * @return 셀러 ID 일치 조건
     */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? sellerAddressJpaEntity.sellerId.eq(sellerId) : null;
    }

    /**
     * 주소 유형 일치 조건.
     *
     * @param addressType 주소 유형
     * @return 주소 유형 일치 조건
     */
    public BooleanExpression addressTypeEq(String addressType) {
        return addressType != null ? sellerAddressJpaEntity.addressType.eq(addressType) : null;
    }

    /**
     * 기본 주소 일치 조건.
     *
     * @param defaultAddress 기본 주소 여부
     * @return 기본 주소 일치 조건
     */
    public BooleanExpression defaultAddressEq(Boolean defaultAddress) {
        return defaultAddress != null
                ? sellerAddressJpaEntity.defaultAddress.eq(defaultAddress)
                : null;
    }
}
