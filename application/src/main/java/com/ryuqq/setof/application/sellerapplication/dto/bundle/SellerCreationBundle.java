package com.ryuqq.setof.application.sellerapplication.dto.bundle;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerAuthOutbox;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerContract;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.aggregate.SellerSettlement;
import com.ryuqq.setof.domain.seller.id.SellerId;

/**
 * 셀러 생성 Bundle.
 *
 * <p>입점 신청 승인 시 생성할 Seller 관련 도메인 객체를 묶습니다.
 *
 * @param seller 셀러 기본 정보
 * @param businessInfo 사업자 정보
 * @param address 주소 정보
 * @param sellerCs CS 정보
 * @param sellerContract 계약 정보
 * @param sellerSettlement 정산 정보
 * @param authOutbox 인증 서버 연동 Outbox
 * @author ryu-qqq
 */
public record SellerCreationBundle(
        Seller seller,
        SellerBusinessInfo businessInfo,
        SellerAddress address,
        SellerCs sellerCs,
        SellerContract sellerContract,
        SellerSettlement sellerSettlement,
        SellerAuthOutbox authOutbox) {

    /**
     * SellerId를 설정합니다.
     *
     * <p>Seller persist 후 반환된 ID를 모든 하위 Entity에 설정합니다.
     *
     * @param sellerId 설정할 SellerId
     */
    public void withSellerId(SellerId sellerId) {
        businessInfo.assignSellerId(sellerId);
        address.assignSellerId(sellerId);
        sellerCs.assignSellerId(sellerId);
        sellerContract.assignSellerId(sellerId);
        sellerSettlement.assignSellerId(sellerId);
        authOutbox.assignSellerId(sellerId);
    }

    /**
     * 셀러명 값을 반환합니다.
     *
     * @return 셀러명
     */
    public String sellerNameValue() {
        return seller.sellerNameValue();
    }

    /**
     * 사업자등록번호 값을 반환합니다.
     *
     * @return 사업자등록번호
     */
    public String registrationNumberValue() {
        return businessInfo.registrationNumberValue();
    }
}
