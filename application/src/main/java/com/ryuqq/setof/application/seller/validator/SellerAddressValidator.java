package com.ryuqq.setof.application.seller.validator;

import com.ryuqq.setof.application.seller.manager.SellerAddressReadManager;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

/**
 * SellerAddress Validator.
 *
 * <p>APP-VAL-001: 검증 성공 시 Domain 객체를 반환합니다.
 *
 * <p>APP-VAL-002: 도메인 전용 예외를 발생시킵니다.
 */
@Component
public class SellerAddressValidator {

    private final SellerAddressReadManager readManager;

    public SellerAddressValidator(SellerAddressReadManager readManager) {
        this.readManager = readManager;
    }

    /**
     * 주소 ID로 존재 여부 검증 후 Domain 객체 반환.
     *
     * @param addressId 주소 ID
     * @return SellerAddress 도메인 객체
     */
    public SellerAddress findExistingOrThrow(SellerAddressId addressId) {
        return readManager.getById(addressId);
    }

    /**
     * 셀러 ID로 주소 존재 여부 검증 후 Domain 객체 반환.
     *
     * @param sellerId 셀러 ID
     * @return SellerAddress 도메인 객체
     */
    public SellerAddress findBySellerId(SellerId sellerId) {
        return readManager.getBySellerId(sellerId);
    }
}
