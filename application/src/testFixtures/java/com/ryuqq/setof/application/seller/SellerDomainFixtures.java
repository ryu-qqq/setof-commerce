package com.ryuqq.setof.application.seller;

import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerBusinessInfoId;
import com.ryuqq.setof.domain.seller.id.SellerCsId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.CompanyName;
import com.ryuqq.setof.domain.seller.vo.CsContact;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.DisplayName;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.OperatingHours;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

/**
 * Seller 도메인 객체 테스트 Fixtures.
 *
 * <p>Application 레이어 테스트에서 사용하는 도메인 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SellerDomainFixtures {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    private SellerDomainFixtures() {}

    // ===== Seller =====

    public static Seller activeSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("테스트셀러"),
                DisplayName.of("테스트스토어"),
                LogoUrl.of("http://example.com/logo.png"),
                Description.of("테스트 설명"),
                true,
                null,
                FIXED_NOW,
                FIXED_NOW);
    }

    public static Seller activeSeller(Long id) {
        return Seller.reconstitute(
                SellerId.of(id),
                SellerName.of("테스트셀러" + id),
                DisplayName.of("테스트스토어" + id),
                LogoUrl.of("http://example.com/logo.png"),
                Description.of("테스트 설명"),
                true,
                null,
                FIXED_NOW,
                FIXED_NOW);
    }

    public static Seller inactiveSeller() {
        return Seller.reconstitute(
                SellerId.of(2L),
                SellerName.of("비활성셀러"),
                DisplayName.of("비활성스토어"),
                LogoUrl.of("http://example.com/logo2.png"),
                Description.of("비활성 설명"),
                false,
                null,
                FIXED_NOW,
                FIXED_NOW);
    }

    public static List<Seller> activeSellers() {
        return List.of(activeSeller(1L), activeSeller(2L));
    }

    // ===== SellerBusinessInfo =====

    public static SellerBusinessInfo sellerBusinessInfo(Long sellerId) {
        return SellerBusinessInfo.reconstitute(
                SellerBusinessInfoId.of(1L),
                SellerId.of(sellerId),
                RegistrationNumber.of("123-45-67890"),
                CompanyName.of("테스트 주식회사"),
                Representative.of("홍길동"),
                SaleReportNumber.of("2024-서울-0001"),
                Address.of("12345", "서울시 강남구 테헤란로 1", "101호"),
                null,
                FIXED_NOW,
                FIXED_NOW);
    }

    // ===== SellerCs =====

    public static SellerCs sellerCs(Long sellerId) {
        return SellerCs.reconstitute(
                SellerCsId.of(1L),
                SellerId.of(sellerId),
                CsContact.of("02-1234-5678", "010-1234-5678", "cs@test.com"),
                OperatingHours.of(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                "MON,TUE,WED,THU,FRI",
                null,
                FIXED_NOW,
                FIXED_NOW);
    }
}
