package com.ryuqq.setof.domain.seller;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddressUpdateData;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfoUpdateData;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.aggregate.SellerCsUpdateData;
import com.ryuqq.setof.domain.seller.aggregate.SellerUpdateData;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerBusinessInfoId;
import com.ryuqq.setof.domain.seller.id.SellerCsId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.AddressName;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import com.ryuqq.setof.domain.seller.vo.CompanyName;
import com.ryuqq.setof.domain.seller.vo.ContactInfo;
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

/**
 * Seller 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Seller 관련 객체들을 생성합니다.
 */
public final class SellerFixtures {

    private SellerFixtures() {}

    // ===== SellerName Fixtures =====
    public static SellerName sellerName(String value) {
        return SellerName.of(value);
    }

    public static SellerName defaultSellerName() {
        return SellerName.of("테스트 셀러");
    }

    // ===== DisplayName Fixtures =====
    public static DisplayName displayName(String value) {
        return DisplayName.of(value);
    }

    public static DisplayName defaultDisplayName() {
        return DisplayName.of("테스트 셀러 스토어");
    }

    // ===== LogoUrl Fixtures =====
    public static LogoUrl logoUrl(String value) {
        return LogoUrl.of(value);
    }

    public static LogoUrl defaultLogoUrl() {
        return LogoUrl.of("https://example.com/logo.png");
    }

    public static LogoUrl emptyLogoUrl() {
        return LogoUrl.empty();
    }

    // ===== Description Fixtures =====
    public static Description description(String value) {
        return Description.of(value);
    }

    public static Description defaultDescription() {
        return Description.of("테스트 셀러 설명입니다.");
    }

    public static Description emptyDescription() {
        return Description.empty();
    }

    // ===== Seller Aggregate Fixtures =====
    public static Seller newSeller() {
        return Seller.forNew(
                defaultSellerName(),
                defaultDisplayName(),
                defaultLogoUrl(),
                defaultDescription(),
                CommonVoFixtures.now());
    }

    public static Seller newSeller(SellerName name, DisplayName displayName) {
        return Seller.forNew(
                name, displayName, defaultLogoUrl(), defaultDescription(), CommonVoFixtures.now());
    }

    public static Seller activeSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                defaultSellerName(),
                defaultDisplayName(),
                defaultLogoUrl(),
                defaultDescription(),
                true,
                null,
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Seller activeSeller(Long id) {
        return Seller.reconstitute(
                SellerId.of(id),
                defaultSellerName(),
                defaultDisplayName(),
                defaultLogoUrl(),
                defaultDescription(),
                true,
                null,
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Seller activeSellerWithAuth(Long id) {
        return Seller.reconstitute(
                SellerId.of(id),
                defaultSellerName(),
                defaultDisplayName(),
                defaultLogoUrl(),
                defaultDescription(),
                true,
                null,
                "tenant-123",
                "org-456",
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Seller inactiveSeller() {
        return Seller.reconstitute(
                SellerId.of(2L),
                defaultSellerName(),
                defaultDisplayName(),
                defaultLogoUrl(),
                defaultDescription(),
                false,
                null,
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Seller deletedSeller() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return Seller.reconstitute(
                SellerId.of(3L),
                defaultSellerName(),
                defaultDisplayName(),
                defaultLogoUrl(),
                defaultDescription(),
                false,
                deletedAt,
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== SellerUpdateData Fixtures =====
    public static SellerUpdateData sellerUpdateData() {
        return SellerUpdateData.of(
                SellerName.of("수정된 셀러명"),
                DisplayName.of("수정된 노출명"),
                LogoUrl.of("https://example.com/new-logo.png"),
                Description.of("수정된 설명입니다."));
    }

    public static SellerUpdateData sellerUpdateData(
            String sellerName, String displayName, String logoUrl, String description) {
        return SellerUpdateData.of(sellerName, displayName, logoUrl, description);
    }

    // ===== SellerBusinessInfo Fixtures =====
    public static RegistrationNumber defaultRegistrationNumber() {
        return RegistrationNumber.of("123-45-67890");
    }

    public static CompanyName defaultCompanyName() {
        return CompanyName.of("테스트 주식회사");
    }

    public static Representative defaultRepresentative() {
        return Representative.of("홍길동");
    }

    public static SaleReportNumber defaultSaleReportNumber() {
        return SaleReportNumber.of("2024-서울강남-0001");
    }

    public static Address defaultBusinessAddress() {
        return Address.of("06141", "서울시 강남구 테헤란로 123", "테스트빌딩 5층");
    }

    public static CsContact defaultCsContact() {
        return CsContact.of("02-1234-5678", "010-1234-5678", "cs@test.com");
    }

    public static SellerBusinessInfo newSellerBusinessInfo() {
        return SellerBusinessInfo.forNew(
                CommonVoFixtures.defaultSellerId(),
                defaultRegistrationNumber(),
                defaultCompanyName(),
                defaultRepresentative(),
                defaultSaleReportNumber(),
                defaultBusinessAddress(),
                CommonVoFixtures.now());
    }

    public static SellerBusinessInfo newSellerBusinessInfo(SellerId sellerId) {
        return SellerBusinessInfo.forNew(
                sellerId,
                defaultRegistrationNumber(),
                defaultCompanyName(),
                defaultRepresentative(),
                defaultSaleReportNumber(),
                defaultBusinessAddress(),
                CommonVoFixtures.now());
    }

    public static SellerBusinessInfo activeSellerBusinessInfo() {
        return SellerBusinessInfo.reconstitute(
                SellerBusinessInfoId.of(1L),
                CommonVoFixtures.defaultSellerId(),
                defaultRegistrationNumber(),
                defaultCompanyName(),
                defaultRepresentative(),
                defaultSaleReportNumber(),
                defaultBusinessAddress(),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static SellerBusinessInfo deletedSellerBusinessInfo() {
        return SellerBusinessInfo.reconstitute(
                SellerBusinessInfoId.of(2L),
                CommonVoFixtures.defaultSellerId(),
                defaultRegistrationNumber(),
                defaultCompanyName(),
                defaultRepresentative(),
                defaultSaleReportNumber(),
                defaultBusinessAddress(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static SellerBusinessInfoUpdateData sellerBusinessInfoUpdateData() {
        return SellerBusinessInfoUpdateData.of(
                RegistrationNumber.of("987-65-43210"),
                CompanyName.of("수정된 주식회사"),
                Representative.of("김철수"),
                SaleReportNumber.of("2024-서울강남-0002"),
                Address.of("06142", "서울시 강남구 역삼로 456", "수정빌딩 10층"));
    }

    // ===== SellerAddress Fixtures =====
    public static AddressName defaultAddressName() {
        return AddressName.of("본사 창고");
    }

    public static ContactInfo defaultContactInfo() {
        return ContactInfo.of("김담당", "010-9876-5432");
    }

    public static SellerAddress newShippingAddress() {
        return SellerAddress.forNew(
                CommonVoFixtures.defaultSellerId(),
                AddressType.SHIPPING,
                defaultAddressName(),
                defaultBusinessAddress(),
                defaultContactInfo(),
                true,
                CommonVoFixtures.now());
    }

    public static SellerAddress newReturnAddress() {
        return SellerAddress.forNew(
                CommonVoFixtures.defaultSellerId(),
                AddressType.RETURN,
                AddressName.of("반품 센터"),
                Address.of("12345", "경기도 성남시 분당구 판교로 789", "물류센터 1층"),
                ContactInfo.of("박담당", "010-1111-2222"),
                false,
                CommonVoFixtures.now());
    }

    public static SellerAddress activeShippingAddress() {
        return SellerAddress.reconstitute(
                SellerAddressId.of(1L),
                CommonVoFixtures.defaultSellerId(),
                AddressType.SHIPPING,
                defaultAddressName(),
                defaultBusinessAddress(),
                defaultContactInfo(),
                true,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static SellerAddress activeReturnAddress() {
        return SellerAddress.reconstitute(
                SellerAddressId.of(2L),
                CommonVoFixtures.defaultSellerId(),
                AddressType.RETURN,
                AddressName.of("반품 센터"),
                Address.of("12345", "경기도 성남시 분당구 판교로 789", "물류센터 1층"),
                ContactInfo.of("박담당", "010-1111-2222"),
                false,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static SellerAddress deletedSellerAddress() {
        return SellerAddress.reconstitute(
                SellerAddressId.of(3L),
                CommonVoFixtures.defaultSellerId(),
                AddressType.SHIPPING,
                defaultAddressName(),
                defaultBusinessAddress(),
                defaultContactInfo(),
                false,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static SellerAddressUpdateData sellerAddressUpdateData() {
        return SellerAddressUpdateData.of(
                AddressName.of("수정된 창고"),
                Address.of("54321", "부산시 해운대구 센텀로 100", "수정 물류센터"),
                ContactInfo.of("최담당", "010-3333-4444"));
    }

    // ===== SellerCs Fixtures =====
    public static SellerCs newSellerCs() {
        return SellerCs.forNew(
                defaultCsContact(),
                OperatingHours.businessHours(),
                "MON,TUE,WED,THU,FRI",
                null,
                CommonVoFixtures.now());
    }

    public static SellerCs newSellerCs(SellerId sellerId) {
        return SellerCs.forNew(
                sellerId,
                defaultCsContact(),
                OperatingHours.businessHours(),
                "MON,TUE,WED,THU,FRI",
                null,
                CommonVoFixtures.now());
    }

    public static SellerCs activeSellerCs() {
        return SellerCs.reconstitute(
                SellerCsId.of(1L),
                CommonVoFixtures.defaultSellerId(),
                defaultCsContact(),
                OperatingHours.businessHours(),
                "MON,TUE,WED,THU,FRI",
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static SellerCsUpdateData sellerCsUpdateData() {
        return SellerCsUpdateData.of(
                CsContact.of("02-9999-8888", "010-9999-8888", "updated-cs@test.com"),
                OperatingHours.businessHours(),
                "월,화,수,목,금",
                "https://pf.kakao.com/updated");
    }
}
