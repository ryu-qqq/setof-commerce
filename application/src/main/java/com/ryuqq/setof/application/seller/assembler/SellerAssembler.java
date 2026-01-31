package com.ryuqq.setof.application.seller.assembler;

import com.ryuqq.setof.application.seller.dto.response.SellerAddressResult;
import com.ryuqq.setof.application.seller.dto.response.SellerAdminResult;
import com.ryuqq.setof.application.seller.dto.response.SellerBusinessInfoResult;
import com.ryuqq.setof.application.seller.dto.response.SellerCustomerResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Seller Assembler.
 *
 * <p>Domain → Result 변환 및 PageResult 생성을 담당합니다.
 */
@Component
public class SellerAssembler {

    /**
     * Domain → SellerResult 변환.
     *
     * @param seller Seller 도메인 객체
     * @return SellerResult
     */
    public SellerResult toResult(Seller seller) {
        return SellerResult.from(seller);
    }

    /**
     * Domain List → SellerResult List 변환.
     *
     * @param sellers Seller 도메인 객체 목록
     * @return SellerResult 목록
     */
    public List<SellerResult> toResults(List<Seller> sellers) {
        return sellers.stream().map(this::toResult).toList();
    }

    /**
     * Domain → SellerBusinessInfoResult 변환.
     *
     * @param businessInfo SellerBusinessInfo 도메인 객체
     * @return SellerBusinessInfoResult
     */
    public SellerBusinessInfoResult toBusinessInfoResult(SellerBusinessInfo businessInfo) {
        return SellerBusinessInfoResult.from(businessInfo);
    }

    /**
     * Domain → SellerAddressResult 변환.
     *
     * @param address SellerAddress 도메인 객체
     * @return SellerAddressResult
     */
    public SellerAddressResult toAddressResult(SellerAddress address) {
        return SellerAddressResult.from(address);
    }

    /**
     * 어드민용 상세 조회 결과 생성. (모두 1:1 관계)
     *
     * @param seller Seller 도메인 객체
     * @param businessInfo SellerBusinessInfo 도메인 객체
     * @param address SellerAddress 도메인 객체
     * @return SellerAdminResult
     */
    public SellerAdminResult toAdminResult(
            Seller seller, SellerBusinessInfo businessInfo, SellerAddress address) {
        return SellerAdminResult.of(
                toResult(seller), toBusinessInfoResult(businessInfo), toAddressResult(address));
    }

    /**
     * 고객용 조회 결과 생성.
     *
     * @param seller Seller 도메인 객체
     * @param businessInfo SellerBusinessInfo 도메인 객체
     * @param sellerCs SellerCs 도메인 객체
     * @return SellerCustomerResult
     */
    public SellerCustomerResult toCustomerResult(
            Seller seller, SellerBusinessInfo businessInfo, SellerCs sellerCs) {
        return SellerCustomerResult.of(seller, businessInfo, sellerCs);
    }

    /**
     * 페이지 결과 생성.
     *
     * @param sellers Seller 도메인 객체 목록
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param totalCount 전체 개수
     * @return SellerPageResult
     */
    public SellerPageResult toPageResult(
            List<Seller> sellers, int page, int size, long totalCount) {
        List<SellerResult> results = toResults(sellers);
        return SellerPageResult.of(results, totalCount, page, size);
    }
}
