package com.ryuqq.setof.adapter.in.rest.admin.sellerapplication;

import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.ApplySellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.RejectSellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.query.SearchSellerApplicationsApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.response.SellerApplicationApiResponse;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationPageResult;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationResult;
import java.time.Instant;
import java.util.List;

/**
 * SellerApplicationApiFixtures - 셀러 입점 신청 API 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class SellerApplicationApiFixtures {

    private SellerApplicationApiFixtures() {}

    public static ApplySellerApplicationApiRequest applyRequest() {
        return new ApplySellerApplicationApiRequest(
                sellerInfo(),
                businessInfo(),
                csContactInfo(),
                returnAddressInfo(),
                settlementInfo());
    }

    public static ApplySellerApplicationApiRequest.SettlementInfo settlementInfo() {
        return new ApplySellerApplicationApiRequest.SettlementInfo(
                "088", "신한은행", "110123456789", "홍길동", "MONTHLY", 1);
    }

    public static ApplySellerApplicationApiRequest.SellerInfo sellerInfo() {
        return new ApplySellerApplicationApiRequest.SellerInfo(
                "테스트셀러", "테스트 브랜드", "https://example.com/logo.png", "테스트 셀러 설명입니다.");
    }

    public static ApplySellerApplicationApiRequest.BusinessInfo businessInfo() {
        return new ApplySellerApplicationApiRequest.BusinessInfo(
                "123-45-67890", "테스트컴퍼니", "홍길동", "제2025-서울강남-1234호", addressDetail());
    }

    public static ApplySellerApplicationApiRequest.CsContactInfo csContactInfo() {
        return new ApplySellerApplicationApiRequest.CsContactInfo(
                "02-1234-5678", "cs@example.com", "010-1234-5678");
    }

    public static ApplySellerApplicationApiRequest.AddressInfo returnAddressInfo() {
        return new ApplySellerApplicationApiRequest.AddressInfo(
                "RETURN", "반품지", addressDetail(), contactInfo());
    }

    public static ApplySellerApplicationApiRequest.AddressDetail addressDetail() {
        return new ApplySellerApplicationApiRequest.AddressDetail("12345", "서울시 강남구", "테헤란로 123");
    }

    public static ApplySellerApplicationApiRequest.ContactInfo contactInfo() {
        return new ApplySellerApplicationApiRequest.ContactInfo("홍길동", "010-1234-5678");
    }

    public static RejectSellerApplicationApiRequest rejectRequest() {
        return new RejectSellerApplicationApiRequest("서류 미비로 인한 반려");
    }

    public static SearchSellerApplicationsApiRequest searchRequest() {
        return new SearchSellerApplicationsApiRequest(
                List.of("PENDING"), null, null, null, null, 0, 20);
    }

    public static SellerApplicationResult applicationResult(Long applicationId) {
        return new SellerApplicationResult(
                applicationId,
                new SellerApplicationResult.SellerInfoResult(
                        "테스트셀러", "테스트 브랜드", "https://example.com/logo.png", "테스트 셀러 설명입니다."),
                new SellerApplicationResult.BusinessInfoResult(
                        "123-45-67890",
                        "테스트컴퍼니",
                        "홍길동",
                        "제2025-서울강남-1234호",
                        new SellerApplicationResult.AddressResult("12345", "서울시 강남구", "테헤란로 123")),
                new SellerApplicationResult.CsContactResult(
                        "02-1234-5678", "cs@example.com", "010-1234-5678"),
                new SellerApplicationResult.AddressInfoResult(
                        "RETURN",
                        "반품지",
                        new SellerApplicationResult.AddressResult("12345", "서울시 강남구", "테헤란로 123"),
                        new SellerApplicationResult.ContactInfoResult("홍길동", "010-1234-5678")),
                null,
                "PENDING",
                Instant.now(),
                null,
                null,
                null,
                null);
    }

    public static SellerApplicationPageResult applicationPageResult() {
        return SellerApplicationPageResult.of(
                List.of(applicationResult(1L), applicationResult(2L)), 2L, 0, 20);
    }

    // ===== API Response Fixtures =====

    public static SellerApplicationApiResponse sellerApplicationApiResponse(Long applicationId) {
        return new SellerApplicationApiResponse(
                applicationId,
                sellerInfoApiResponse(),
                businessInfoApiResponse(),
                csContactInfoApiResponse(),
                addressInfoApiResponse(),
                agreementInfoApiResponse(),
                "PENDING",
                "2025-01-23T10:30:00+09:00",
                null,
                null,
                null,
                null);
    }

    public static SellerApplicationApiResponse sellerApplicationApiResponseApproved(
            Long applicationId) {
        return new SellerApplicationApiResponse(
                applicationId,
                sellerInfoApiResponse(),
                businessInfoApiResponse(),
                csContactInfoApiResponse(),
                addressInfoApiResponse(),
                agreementInfoApiResponse(),
                "APPROVED",
                "2025-01-23T10:30:00+09:00",
                "2025-01-24T15:00:00+09:00",
                "admin@example.com",
                null,
                100L);
    }

    public static SellerApplicationApiResponse sellerApplicationApiResponseRejected(
            Long applicationId) {
        return new SellerApplicationApiResponse(
                applicationId,
                sellerInfoApiResponse(),
                businessInfoApiResponse(),
                csContactInfoApiResponse(),
                addressInfoApiResponse(),
                agreementInfoApiResponse(),
                "REJECTED",
                "2025-01-23T10:30:00+09:00",
                "2025-01-24T15:00:00+09:00",
                "admin@example.com",
                "서류 미비로 인한 반려",
                null);
    }

    public static SellerApplicationApiResponse.SellerInfo sellerInfoApiResponse() {
        return new SellerApplicationApiResponse.SellerInfo(
                "테스트셀러", "테스트 브랜드", "https://example.com/logo.png", "테스트 셀러 설명입니다.");
    }

    public static SellerApplicationApiResponse.BusinessInfo businessInfoApiResponse() {
        return new SellerApplicationApiResponse.BusinessInfo(
                "123-45-67890", "테스트컴퍼니", "홍길동", "제2025-서울강남-1234호", addressDetailApiResponse());
    }

    public static SellerApplicationApiResponse.CsContactInfo csContactInfoApiResponse() {
        return new SellerApplicationApiResponse.CsContactInfo(
                "02-1234-5678", "cs@example.com", "010-1234-5678");
    }

    public static SellerApplicationApiResponse.AddressInfo addressInfoApiResponse() {
        return new SellerApplicationApiResponse.AddressInfo(
                "RETURN", "반품지", addressDetailApiResponse(), contactInfoApiResponse());
    }

    public static SellerApplicationApiResponse.AddressDetail addressDetailApiResponse() {
        return new SellerApplicationApiResponse.AddressDetail("12345", "서울시 강남구", "테헤란로 123");
    }

    public static SellerApplicationApiResponse.ContactInfo contactInfoApiResponse() {
        return new SellerApplicationApiResponse.ContactInfo("홍길동", "010-1234-5678");
    }

    public static SellerApplicationApiResponse.AgreementInfo agreementInfoApiResponse() {
        return new SellerApplicationApiResponse.AgreementInfo(
                "2025-01-23T10:30:00+09:00", true, true);
    }
}
