package com.ryuqq.setof.adapter.in.rest.admin.sellerapplication;

import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.ApplySellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.ApproveSellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.RejectSellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.query.SearchSellerApplicationsApiRequest;
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
                sellerInfo(), businessInfo(), csContactInfo(), returnAddressInfo());
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

    public static ApproveSellerApplicationApiRequest approveRequest() {
        return new ApproveSellerApplicationApiRequest("ADMIN_USER");
    }

    public static RejectSellerApplicationApiRequest rejectRequest() {
        return new RejectSellerApplicationApiRequest("서류 미비로 인한 반려", "ADMIN_USER");
    }

    public static SearchSellerApplicationsApiRequest searchRequest() {
        return new SearchSellerApplicationsApiRequest("PENDING", null, null, null, null, 0, 20);
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
}
