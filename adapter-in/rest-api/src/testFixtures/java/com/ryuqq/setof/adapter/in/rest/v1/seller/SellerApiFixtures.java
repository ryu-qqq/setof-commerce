package com.ryuqq.setof.adapter.in.rest.v1.seller;

import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult.AddressInfo;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult.BusinessInfo;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult.CsInfo;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult.SellerInfo;
import java.time.Instant;

/**
 * Seller V1 API 테스트 Fixtures.
 *
 * <p>Seller 관련 API Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class SellerApiFixtures {

    private SellerApiFixtures() {}

    // ===== SellerV1ApiResponse (레거시 flat 구조) =====

    public static SellerV1ApiResponse sellerResponse(long sellerId) {
        return new SellerV1ApiResponse(
                sellerId,
                "나이키코리아 유한회사",
                "https://cdn.example.com/sellers/nike.png",
                "나이키 공식 판매처",
                "서울특별시 강남구 테헤란로 123 4층 06234",
                "1588-0000",
                "010-1234-5678",
                "123-45-67890",
                "2024-서울강남-12345",
                "홍길동",
                "cs@nike.co.kr");
    }

    public static SellerV1ApiResponse sellerResponseWithoutCsInfo(long sellerId) {
        return new SellerV1ApiResponse(
                sellerId,
                "나이키코리아 유한회사",
                "https://cdn.example.com/sellers/nike.png",
                "나이키 공식 판매처",
                "서울특별시 강남구 테헤란로 123 4층 06234",
                "",
                "",
                "123-45-67890",
                "2024-서울강남-12345",
                "홍길동",
                "");
    }

    public static SellerV1ApiResponse sellerResponseWithoutBusinessInfo(long sellerId) {
        return new SellerV1ApiResponse(
                sellerId,
                "",
                "https://cdn.example.com/sellers/nike.png",
                "나이키 공식 판매처",
                "",
                "1588-0000",
                "010-1234-5678",
                "",
                "",
                "",
                "cs@nike.co.kr");
    }

    public static SellerV1ApiResponse sellerResponseWithNullOptionalFields(long sellerId) {
        return new SellerV1ApiResponse(
                sellerId,
                "",
                "https://cdn.example.com/sellers/nike.png",
                "나이키 공식 판매처",
                "",
                "",
                "",
                "",
                "",
                "",
                "");
    }

    // ===== SellerCompositeResult =====

    public static SellerCompositeResult sellerCompositeResult(long sellerId) {
        return new SellerCompositeResult(
                sellerInfo(sellerId), addressInfo(1L), businessInfo(1L), csInfo(1L));
    }

    public static SellerCompositeResult sellerCompositeResultWithoutCsInfo(long sellerId) {
        return new SellerCompositeResult(
                sellerInfo(sellerId), addressInfo(1L), businessInfo(1L), null);
    }

    public static SellerCompositeResult sellerCompositeResultWithoutBusinessInfo(long sellerId) {
        return new SellerCompositeResult(sellerInfo(sellerId), addressInfo(1L), null, csInfo(1L));
    }

    public static SellerCompositeResult sellerCompositeResultWithNullOptionalFields(long sellerId) {
        return new SellerCompositeResult(sellerInfo(sellerId), addressInfo(1L), null, null);
    }

    public static SellerCompositeResult sellerCompositeResultWithNullId() {
        return new SellerCompositeResult(
                sellerInfoWithNullId(), addressInfo(1L), businessInfo(1L), csInfo(1L));
    }

    // ===== SellerInfo =====

    public static SellerInfo sellerInfo(long sellerId) {
        return new SellerInfo(
                sellerId,
                "나이키코리아",
                "나이키 공식스토어",
                "https://cdn.example.com/sellers/nike.png",
                "나이키 공식 판매처",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static SellerInfo sellerInfoWithNullId() {
        return new SellerInfo(
                null,
                "나이키코리아",
                "나이키 공식스토어",
                "https://cdn.example.com/sellers/nike.png",
                "나이키 공식 판매처",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    // ===== AddressInfo =====

    public static AddressInfo addressInfo(long id) {
        return new AddressInfo(
                id,
                "RETURN",
                "본사 반품센터",
                "06234",
                "서울특별시 강남구 테헤란로 123",
                "4층",
                "김담당",
                "02-1234-5678",
                true);
    }

    // ===== BusinessInfo =====

    public static BusinessInfo businessInfo(long id) {
        return new BusinessInfo(
                id,
                "123-45-67890",
                "나이키코리아 유한회사",
                "홍길동",
                "2024-서울강남-12345",
                "06234",
                "서울특별시 강남구 테헤란로 123",
                "4층");
    }

    // ===== CsInfo =====

    public static CsInfo csInfo(long id) {
        return new CsInfo(
                id,
                "1588-0000",
                "010-1234-5678",
                "cs@nike.co.kr",
                "09:00",
                "18:00",
                "월~금",
                "https://pf.kakao.com/nike");
    }
}
