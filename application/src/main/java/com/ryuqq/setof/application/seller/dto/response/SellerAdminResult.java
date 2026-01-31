package com.ryuqq.setof.application.seller.dto.response;

/**
 * 어드민용 셀러 상세 조회 결과.
 *
 * <p>Seller + BusinessInfo + Address 모두 포함 (모두 1:1 관계)
 *
 * @param seller 셀러 기본 정보
 * @param businessInfo 사업자 정보
 * @param address 주소
 */
public record SellerAdminResult(
        SellerResult seller, SellerBusinessInfoResult businessInfo, SellerAddressResult address) {

    public static SellerAdminResult of(
            SellerResult seller,
            SellerBusinessInfoResult businessInfo,
            SellerAddressResult address) {
        return new SellerAdminResult(seller, businessInfo, address);
    }
}
