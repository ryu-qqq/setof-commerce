package com.ryuqq.setof.adapter.in.rest.admin.v1.seller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.BusinessValidationV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SearchSellersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.AddressInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.BusinessInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.ContractInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.CsInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.SellerInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.SettlementInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Seller Admin V1 API 테스트 Fixtures.
 *
 * <p>Seller Admin 관련 API Request/Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class SellerAdminApiFixtures {

    private SellerAdminApiFixtures() {}

    // ===== SearchSellersV1ApiRequest =====

    public static SearchSellersV1ApiRequest searchRequest() {
        return new SearchSellersV1ApiRequest("SELLER_NAME", "테스트셀러", null, null, 0, 20);
    }

    public static SearchSellersV1ApiRequest searchRequestById() {
        return new SearchSellersV1ApiRequest("SELLER_ID", "1", null, null, 0, 20);
    }

    public static SearchSellersV1ApiRequest searchRequestNullKeyword() {
        return new SearchSellersV1ApiRequest(null, "테스트", null, null, 0, 20);
    }

    public static SearchSellersV1ApiRequest searchRequestBlankKeyword() {
        return new SearchSellersV1ApiRequest("   ", "테스트", null, null, 0, 20);
    }

    public static SearchSellersV1ApiRequest searchRequestNullSearchWord() {
        return new SearchSellersV1ApiRequest("SELLER_NAME", null, null, null, 0, 20);
    }

    public static SearchSellersV1ApiRequest searchRequestBlankSearchWord() {
        return new SearchSellersV1ApiRequest("SELLER_NAME", "   ", null, null, 0, 20);
    }

    public static SearchSellersV1ApiRequest searchRequestDefaultValues() {
        return new SearchSellersV1ApiRequest(null, null, null, null, null, null);
    }

    public static SearchSellersV1ApiRequest searchRequestWithPage(int page, int size) {
        return new SearchSellersV1ApiRequest("SELLER_NAME", "테스트", null, null, page, size);
    }

    // ===== BusinessValidationV1ApiRequest =====

    public static BusinessValidationV1ApiRequest businessValidationRequest() {
        return new BusinessValidationV1ApiRequest("123-45-67890");
    }

    public static BusinessValidationV1ApiRequest businessValidationRequestWithoutHyphen() {
        return new BusinessValidationV1ApiRequest("1234567890");
    }

    // ===== SellerV1ApiResponse =====

    public static SellerV1ApiResponse sellerResponse(long sellerId) {
        return new SellerV1ApiResponse(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://cdn.example.com/sellers/test.png",
                "테스트 셀러입니다",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static SellerV1ApiResponse sellerResponse(
            long sellerId, String sellerName, String displayName) {
        return new SellerV1ApiResponse(
                sellerId,
                sellerName,
                displayName,
                "https://cdn.example.com/sellers/test.png",
                "테스트 셀러입니다",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static List<SellerV1ApiResponse> sellerResponseList() {
        return List.of(sellerResponse(1L), sellerResponse(2L, "셀러2", "브랜드2"));
    }

    // ===== SellerResult =====

    public static SellerResult sellerResult(Long sellerId) {
        return new SellerResult(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://cdn.example.com/sellers/test.png",
                "테스트 셀러입니다",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static SellerResult sellerResultNullId() {
        return new SellerResult(
                null,
                "테스트셀러",
                "테스트 브랜드",
                null,
                null,
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static List<SellerResult> sellerResultList() {
        return List.of(sellerResult(1L), sellerResult(2L));
    }

    // ===== SellerPageResult =====

    public static SellerPageResult sellerPageResult() {
        return SellerPageResult.of(sellerResultList(), 0, 20, 2L);
    }

    public static SellerPageResult sellerPageResult(int page, int size, long totalCount) {
        return SellerPageResult.of(sellerResultList(), page, size, totalCount);
    }

    public static SellerPageResult emptySellerPageResult() {
        return SellerPageResult.of(List.of(), 0, 20, 0L);
    }

    // ===== CustomPageableV1ApiResponse =====

    public static CustomPageableV1ApiResponse<SellerV1ApiResponse> sellerPageResponse() {
        return CustomPageableV1ApiResponse.of(sellerResponseList(), 0, 20, 2L);
    }

    public static CustomPageableV1ApiResponse<SellerV1ApiResponse> sellerPageResponse(
            int page, int size, long totalElements) {
        return CustomPageableV1ApiResponse.of(sellerResponseList(), page, size, totalElements);
    }

    public static CustomPageableV1ApiResponse<SellerV1ApiResponse> emptySellerPageResponse() {
        return CustomPageableV1ApiResponse.of(List.of(), 0, 20, 0L);
    }

    // ===== SellerFullCompositeResult =====

    public static SellerFullCompositeResult sellerFullCompositeResult(Long sellerId) {
        SellerCompositeResult.SellerInfo sellerInfo = sellerInfo(sellerId);
        SellerCompositeResult.AddressInfo addressInfo = addressInfo(1L);
        SellerCompositeResult.BusinessInfo businessInfo = businessInfo(1L);
        SellerCompositeResult.CsInfo csInfo = csInfo(1L);

        SellerCompositeResult sellerComposite =
                new SellerCompositeResult(sellerInfo, addressInfo, businessInfo, csInfo);

        SellerFullCompositeResult.ContractInfo contractInfo = contractInfo(1L);
        SellerFullCompositeResult.SettlementInfo settlementInfo = settlementInfo(1L);

        return new SellerFullCompositeResult(sellerComposite, null, contractInfo, settlementInfo);
    }

    public static SellerFullCompositeResult sellerFullCompositeResultNullAddress(Long sellerId) {
        SellerCompositeResult.SellerInfo sellerInfo = sellerInfo(sellerId);

        SellerCompositeResult sellerComposite =
                new SellerCompositeResult(sellerInfo, null, null, null);

        return new SellerFullCompositeResult(sellerComposite, null, null, null);
    }

    // ===== SellerDetailV1ApiResponse =====

    public static SellerDetailV1ApiResponse sellerDetailApiResponse(Long sellerId) {
        return new SellerDetailV1ApiResponse(
                sellerInfoResponse(sellerId),
                addressInfoResponse(1L),
                businessInfoResponse(1L),
                csInfoResponse(1L),
                contractInfoResponse(1L),
                settlementInfoResponse(1L));
    }

    // ===== Nested Info Records =====

    public static SellerCompositeResult.SellerInfo sellerInfo(Long sellerId) {
        return new SellerCompositeResult.SellerInfo(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://cdn.example.com/sellers/test.png",
                "테스트 셀러입니다",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static SellerCompositeResult.AddressInfo addressInfo(Long addressId) {
        return new SellerCompositeResult.AddressInfo(
                addressId,
                "BUSINESS",
                "본사",
                "12345",
                "서울시 강남구 테헤란로 123",
                "4층",
                "홍길동",
                "02-1234-5678",
                true);
    }

    public static SellerCompositeResult.BusinessInfo businessInfo(Long businessInfoId) {
        return new SellerCompositeResult.BusinessInfo(
                businessInfoId,
                "1234567890",
                "테스트주식회사",
                "홍길동",
                "2024-서울강남-12345",
                "12345",
                "서울시 강남구 테헤란로 123",
                "4층");
    }

    public static SellerCompositeResult.CsInfo csInfo(Long csInfoId) {
        return new SellerCompositeResult.CsInfo(
                csInfoId,
                "02-1234-5678",
                "010-1234-5678",
                "cs@test.com",
                "09:00",
                "18:00",
                "월-금",
                "https://pf.kakao.com/test");
    }

    public static SellerFullCompositeResult.ContractInfo contractInfo(Long contractId) {
        return new SellerFullCompositeResult.ContractInfo(
                contractId,
                BigDecimal.valueOf(5.0),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2025, 12, 31),
                "ACTIVE",
                "특약사항 없음",
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static SellerFullCompositeResult.SettlementInfo settlementInfo(Long settlementId) {
        return new SellerFullCompositeResult.SettlementInfo(
                settlementId,
                "004",
                "KB국민은행",
                "123-456789-01-001",
                "테스트주식회사",
                "WEEKLY",
                5,
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    // ===== Response Records =====

    public static SellerInfoResponse sellerInfoResponse(Long sellerId) {
        return new SellerInfoResponse(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://cdn.example.com/sellers/test.png",
                "테스트 셀러입니다",
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static AddressInfoResponse addressInfoResponse(Long addressId) {
        return new AddressInfoResponse(
                addressId,
                "BUSINESS",
                "본사",
                "12345",
                "서울시 강남구 테헤란로 123",
                "4층",
                "홍길동",
                "02-1234-5678",
                true);
    }

    public static BusinessInfoResponse businessInfoResponse(Long businessInfoId) {
        return new BusinessInfoResponse(
                businessInfoId,
                "1234567890",
                "테스트주식회사",
                "홍길동",
                "2024-서울강남-12345",
                "12345",
                "서울시 강남구 테헤란로 123",
                "4층");
    }

    public static CsInfoResponse csInfoResponse(Long csInfoId) {
        return new CsInfoResponse(
                csInfoId,
                "02-1234-5678",
                "010-1234-5678",
                "cs@test.com",
                "09:00",
                "18:00",
                "월-금",
                "https://pf.kakao.com/test");
    }

    public static ContractInfoResponse contractInfoResponse(Long contractId) {
        return new ContractInfoResponse(
                contractId,
                BigDecimal.valueOf(5.0),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2025, 12, 31),
                "ACTIVE",
                "특약사항 없음",
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }

    public static SettlementInfoResponse settlementInfoResponse(Long settlementId) {
        return new SettlementInfoResponse(
                settlementId,
                "004",
                "KB국민은행",
                "123-456789-01-001",
                "테스트주식회사",
                "WEEKLY",
                5,
                true,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z"));
    }
}
