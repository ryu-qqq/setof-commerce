package com.ryuqq.setof.domain.claim.vo;

import com.ryuqq.setof.domain.common.vo.Money;

/**
 * 수거 배송비 정보 Value Object.
 *
 * <p>수거 배송비 금액과 부담자 정보를 포함합니다.
 *
 * @param shippingFee 배송비
 * @param feePayer 배송비 부담자
 */
public record ShippingFeeInfo(Money shippingFee, FeePayer feePayer) {

    public ShippingFeeInfo {
        if (shippingFee == null) {
            throw new IllegalArgumentException("배송비는 필수입니다");
        }
        if (feePayer == null) {
            throw new IllegalArgumentException("배송비 부담자는 필수입니다");
        }
    }

    public static ShippingFeeInfo of(Money shippingFee, FeePayer feePayer) {
        return new ShippingFeeInfo(shippingFee, feePayer);
    }

    /**
     * 무료 배송비 정보를 생성합니다. 판매자 부담으로 설정됩니다.
     *
     * @return 무료 배송비 정보
     */
    public static ShippingFeeInfo free() {
        return new ShippingFeeInfo(Money.zero(), FeePayer.SELLER);
    }

    /**
     * 배송비가 무료인지 확인합니다.
     *
     * @return 무료 여부
     */
    public boolean isFree() {
        return shippingFee.isZero();
    }

    /**
     * 배송비 금액 값을 반환합니다.
     *
     * @return 배송비 금액 (정수)
     */
    public int shippingFeeValue() {
        return shippingFee.value();
    }

    /**
     * 구매자 부담인지 확인합니다.
     *
     * @return 구매자 부담 여부
     */
    public boolean isBuyerPays() {
        return feePayer == FeePayer.BUYER;
    }

    /**
     * 판매자 부담인지 확인합니다.
     *
     * @return 판매자 부담 여부
     */
    public boolean isSellerPays() {
        return feePayer == FeePayer.SELLER;
    }
}
