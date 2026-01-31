package com.ryuqq.setof.adapter.in.rest.admin.seller;

import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SearchSellersApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * SellerApiFixtures - 셀러 API 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class SellerApiFixtures {

    private SellerApiFixtures() {}

    public static RegisterSellerApiRequest registerRequest() {
        return new RegisterSellerApiRequest(
                sellerInfoRequest(), businessInfoRequest(), addressInfoRequest());
    }

    public static RegisterSellerApiRequest.SellerInfoRequest sellerInfoRequest() {
        return new RegisterSellerApiRequest.SellerInfoRequest(
                "테스트셀러", "테스트 브랜드", "https://example.com/logo.png", "테스트 셀러 설명입니다.");
    }

    public static RegisterSellerApiRequest.BusinessInfoRequest businessInfoRequest() {
        return new RegisterSellerApiRequest.BusinessInfoRequest(
                "123-45-67890",
                "테스트컴퍼니",
                "홍길동",
                "제2025-서울강남-1234호",
                addressRequest(),
                csContactRequest());
    }

    public static RegisterSellerApiRequest.AddressInfoRequest addressInfoRequest() {
        return new RegisterSellerApiRequest.AddressInfoRequest(
                "RETURN", "본사", addressRequest(), contactInfoRequest(), true);
    }

    public static RegisterSellerApiRequest.AddressRequest addressRequest() {
        return new RegisterSellerApiRequest.AddressRequest("12345", "서울시 강남구", "테헤란로 123");
    }

    public static RegisterSellerApiRequest.CsContactRequest csContactRequest() {
        return new RegisterSellerApiRequest.CsContactRequest(
                "02-1234-5678", "cs@example.com", "010-1234-5678");
    }

    public static RegisterSellerApiRequest.ContactInfoRequest contactInfoRequest() {
        return new RegisterSellerApiRequest.ContactInfoRequest("홍길동", "010-1234-5678");
    }

    /**
     * 셀러 기본정보 수정 요청 Fixture.
     *
     * <p>address, csInfo, businessInfo는 필수값입니다.
     */
    public static UpdateSellerApiRequest updateRequest() {
        return new UpdateSellerApiRequest(
                "수정된 셀러명",
                "수정된 표시명",
                "https://example.com/new-logo.png",
                "수정된 설명입니다.",
                new UpdateSellerApiRequest.AddressRequest("12345", "서울시 강남구", "테헤란로 123"),
                new UpdateSellerApiRequest.CsInfoRequest(
                        "02-1234-5678", "cs@example.com", "010-1234-5678"),
                new UpdateSellerApiRequest.BusinessInfoRequest(
                        "123-45-67890",
                        "테스트컴퍼니",
                        "홍길동",
                        "제2025-서울강남-1234호",
                        new UpdateSellerApiRequest.AddressRequest("12345", "서울시 강남구", "테헤란로 456")));
    }

    /**
     * 셀러 기본정보 + 주소/CS/사업자 정보가 다른 값으로 수정된 요청 Fixture.
     *
     * <p>address, csInfo, businessInfo는 필수값입니다.
     */
    public static UpdateSellerApiRequest updateRequestWithAddress() {
        return new UpdateSellerApiRequest(
                "수정된 셀러명",
                "수정된 표시명",
                "https://example.com/new-logo.png",
                "수정된 설명입니다.",
                new UpdateSellerApiRequest.AddressRequest("54321", "부산시 해운대구", "해운대로 456"),
                new UpdateSellerApiRequest.CsInfoRequest(
                        "051-987-6543", "newcs@example.com", "010-9876-5432"),
                new UpdateSellerApiRequest.BusinessInfoRequest(
                        "987-65-43210",
                        "수정된컴퍼니",
                        "김철수",
                        "제2025-부산해운대-5678호",
                        new UpdateSellerApiRequest.AddressRequest(
                                "54321", "부산시 해운대구", "해운대로 789")));
    }

    public static SearchSellersApiRequest searchRequest() {
        return new SearchSellersApiRequest(true, "sellerName", "테스트", null, null, 0, 20);
    }

    public static SellerFullCompositeResult sellerFullCompositeResult(Long sellerId) {
        return new SellerFullCompositeResult(
                sellerCompositeResult(sellerId),
                sellerPolicyCompositeResult(sellerId),
                contractInfo(),
                settlementInfo());
    }

    public static SellerCompositeResult sellerCompositeResult(Long sellerId) {
        return new SellerCompositeResult(
                sellerInfo(sellerId), addressInfo(), businessInfo(), csInfo());
    }

    public static SellerCompositeResult.SellerInfo sellerInfo(Long sellerId) {
        Instant now = Instant.now();
        return new SellerCompositeResult.SellerInfo(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://example.com/logo.png",
                "테스트 셀러 설명입니다.",
                true,
                now,
                now);
    }

    public static SellerCompositeResult.AddressInfo addressInfo() {
        return new SellerCompositeResult.AddressInfo(
                1L, "RETURN", "본사", "12345", "서울시 강남구", "테헤란로 123", "홍길동", "010-1234-5678", true);
    }

    public static SellerCompositeResult.BusinessInfo businessInfo() {
        return new SellerCompositeResult.BusinessInfo(
                1L,
                "123-45-67890",
                "테스트컴퍼니",
                "홍길동",
                "제2025-서울강남-1234호",
                "12345",
                "서울시 강남구",
                "테헤란로 456");
    }

    public static SellerCompositeResult.CsInfo csInfo() {
        return new SellerCompositeResult.CsInfo(
                1L,
                "02-1234-5678",
                "010-1234-5678",
                "cs@example.com",
                "09:00",
                "18:00",
                "월~금",
                null);
    }

    public static SellerPolicyCompositeResult sellerPolicyCompositeResult(Long sellerId) {
        return new SellerPolicyCompositeResult(sellerId, List.of(), List.of());
    }

    public static SellerFullCompositeResult.ContractInfo contractInfo() {
        Instant now = Instant.now();
        return new SellerFullCompositeResult.ContractInfo(
                1L,
                new BigDecimal("10.5"),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                "ACTIVE",
                "신규 셀러 수수료 할인",
                now,
                now);
    }

    public static SellerFullCompositeResult.SettlementInfo settlementInfo() {
        Instant now = Instant.now();
        return new SellerFullCompositeResult.SettlementInfo(
                1L, "088", "신한은행", "123-456-789", "테스트컴퍼니", "MONTHLY", 15, true, now, now, now);
    }

    // ===== API Response Fixtures =====

    public static SellerDetailApiResponse sellerDetailApiResponse(Long sellerId) {
        return new SellerDetailApiResponse(
                sellerInfoApiResponse(sellerId),
                addressInfoApiResponse(),
                businessInfoApiResponse(),
                csInfoApiResponse(),
                contractInfoApiResponse(),
                settlementInfoApiResponse());
    }

    public static SellerDetailApiResponse.SellerInfo sellerInfoApiResponse(Long sellerId) {
        return new SellerDetailApiResponse.SellerInfo(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://example.com/logo.png",
                "테스트 셀러 설명입니다.",
                true,
                "2025-01-23T10:30:00+09:00",
                "2025-01-23T10:30:00+09:00");
    }

    public static SellerDetailApiResponse.AddressInfo addressInfoApiResponse() {
        return new SellerDetailApiResponse.AddressInfo(
                1L, "RETURN", "본사", "12345", "서울시 강남구", "테헤란로 123", "홍길동", "010-1234-5678", true);
    }

    public static SellerDetailApiResponse.BusinessInfo businessInfoApiResponse() {
        return new SellerDetailApiResponse.BusinessInfo(
                1L,
                "123-45-67890",
                "테스트컴퍼니",
                "홍길동",
                "제2025-서울강남-1234호",
                "12345",
                "서울시 강남구",
                "테헤란로 456");
    }

    public static SellerDetailApiResponse.CsInfo csInfoApiResponse() {
        return new SellerDetailApiResponse.CsInfo(
                1L,
                "02-1234-5678",
                "010-1234-5678",
                "cs@example.com",
                "09:00",
                "18:00",
                "MON,TUE,WED,THU,FRI",
                "https://pf.kakao.com/test");
    }

    public static SellerDetailApiResponse.ContractInfo contractInfoApiResponse() {
        return new SellerDetailApiResponse.ContractInfo(
                1L,
                "10.5",
                "2025-01-01",
                "2025-12-31",
                "ACTIVE",
                "신규 셀러 수수료 할인",
                "2025-01-23T10:30:00+09:00",
                "2025-01-23T10:30:00+09:00");
    }

    public static SellerDetailApiResponse.SettlementInfo settlementInfoApiResponse() {
        return new SellerDetailApiResponse.SettlementInfo(
                1L,
                "088",
                "신한은행",
                "123-456-789",
                "테스트컴퍼니",
                "MONTHLY",
                15,
                true,
                "2025-01-23T10:30:00+09:00",
                "2025-01-23T10:30:00+09:00",
                "2025-01-23T10:30:00+09:00");
    }

    public static SellerResult sellerResult(Long sellerId) {
        Instant now = Instant.now();
        return new SellerResult(
                sellerId,
                "테스트셀러",
                "테스트 브랜드",
                "https://example.com/logo.png",
                "테스트 셀러 설명입니다.",
                true,
                now,
                now);
    }

    public static SellerPageResult sellerPageResult() {
        return new SellerPageResult(List.of(sellerResult(1L), sellerResult(2L)), 2L, 0, 20, false);
    }
}
