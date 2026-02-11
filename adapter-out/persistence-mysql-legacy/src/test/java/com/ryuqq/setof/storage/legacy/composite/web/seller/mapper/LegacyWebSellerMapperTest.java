package com.ryuqq.setof.storage.legacy.composite.web.seller.mapper;

import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_ACCOUNT_HOLDER;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_ACCOUNT_NUMBER;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_ADDRESS_LINE1;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_ADDRESS_LINE2;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_BANK_NAME;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_COMMISSION_RATE;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_COMPANY_NAME;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_CREATED_AT;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_CS_EMAIL;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_CS_NUMBER;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_CS_PHONE;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_LOGO_URL;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_REGISTRATION_NUMBER;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_REPRESENTATIVE;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_SALE_REPORT_NUMBER;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_SELLER_DESCRIPTION;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_SELLER_ID;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_SELLER_NAME;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_UPDATED_AT;
import static com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures.DEFAULT_ZIP_CODE;
import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.seller.dto.composite.SellerAdminCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures;
import com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyWebSellerMapper 단위 테스트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyWebSellerMapper 단위 테스트")
class LegacyWebSellerMapperTest {

    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    private LegacyWebSellerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LegacyWebSellerMapper();
    }

    @Nested
    @DisplayName("toCompositeResult(LegacyWebSellerQueryDto) 메서드 테스트")
    class ToCompositeResultFromDtoTest {

        @Test
        @DisplayName("정상적인 DTO를 SellerCompositeResult로 변환합니다")
        void toCompositeResult_WithValidDto_ReturnsSellerCompositeResult() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.defaultDto();

            // when
            SellerCompositeResult result = mapper.toCompositeResult(dto);

            // then
            assertThat(result).isNotNull();

            SellerCompositeResult.SellerInfo sellerInfo = result.seller();
            assertThat(sellerInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(sellerInfo.sellerName()).isEqualTo(DEFAULT_SELLER_NAME);
            assertThat(sellerInfo.displayName()).isEqualTo(DEFAULT_SELLER_NAME);
            assertThat(sellerInfo.logoUrl()).isEqualTo(DEFAULT_LOGO_URL);
            assertThat(sellerInfo.description()).isEqualTo(DEFAULT_SELLER_DESCRIPTION);
            assertThat(sellerInfo.active()).isTrue();
            assertThat(sellerInfo.createdAt())
                    .isEqualTo(DEFAULT_CREATED_AT.atZone(SEOUL_ZONE).toInstant());
            assertThat(sellerInfo.updatedAt())
                    .isEqualTo(DEFAULT_UPDATED_AT.atZone(SEOUL_ZONE).toInstant());

            SellerCompositeResult.AddressInfo addressInfo = result.address();
            assertThat(addressInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(addressInfo.addressType()).isEqualTo("BUSINESS");
            assertThat(addressInfo.addressName()).isNull();
            assertThat(addressInfo.zipcode()).isEqualTo(DEFAULT_ZIP_CODE);
            assertThat(addressInfo.address()).isEqualTo(DEFAULT_ADDRESS_LINE1);
            assertThat(addressInfo.addressDetail()).isEqualTo(DEFAULT_ADDRESS_LINE2);
            assertThat(addressInfo.contactName()).isNull();
            assertThat(addressInfo.contactPhone()).isEqualTo(DEFAULT_CS_PHONE);
            assertThat(addressInfo.defaultAddress()).isTrue();

            SellerCompositeResult.BusinessInfo businessInfo = result.businessInfo();
            assertThat(businessInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(businessInfo.registrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
            assertThat(businessInfo.companyName()).isEqualTo(DEFAULT_COMPANY_NAME);
            assertThat(businessInfo.representative()).isEqualTo(DEFAULT_REPRESENTATIVE);
            assertThat(businessInfo.saleReportNumber()).isEqualTo(DEFAULT_SALE_REPORT_NUMBER);
            assertThat(businessInfo.businessZipcode()).isEqualTo(DEFAULT_ZIP_CODE);
            assertThat(businessInfo.businessAddress()).isEqualTo(DEFAULT_ADDRESS_LINE1);
            assertThat(businessInfo.businessAddressDetail()).isEqualTo(DEFAULT_ADDRESS_LINE2);

            SellerCompositeResult.CsInfo csInfo = result.csInfo();
            assertThat(csInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(csInfo.csPhone()).isEqualTo(DEFAULT_CS_NUMBER);
            assertThat(csInfo.csMobile()).isEqualTo(DEFAULT_CS_PHONE);
            assertThat(csInfo.csEmail()).isEqualTo(DEFAULT_CS_EMAIL);
            assertThat(csInfo.operatingStartTime()).isNull();
            assertThat(csInfo.operatingEndTime()).isNull();
            assertThat(csInfo.operatingDays()).isNull();
            assertThat(csInfo.kakaoChannelUrl()).isNull();
        }

        @Test
        @DisplayName("csNumber가 null일 때 csPhoneNumber로 폴백합니다")
        void toCompositeResult_WithNullCsNumber_FallbackToCsPhoneNumber() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.withNullCsNumber();

            // when
            SellerCompositeResult result = mapper.toCompositeResult(dto);

            // then
            assertThat(result).isNotNull();
            SellerCompositeResult.CsInfo csInfo = result.csInfo();
            assertThat(csInfo.csPhone()).isEqualTo(DEFAULT_CS_PHONE);
        }

        @Test
        @DisplayName("날짜가 null일 때 null Instant를 반환합니다")
        void toCompositeResult_WithNullDates_ReturnsNullInstants() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.withNullDates();

            // when
            SellerCompositeResult result = mapper.toCompositeResult(dto);

            // then
            assertThat(result).isNotNull();
            SellerCompositeResult.SellerInfo sellerInfo = result.seller();
            assertThat(sellerInfo.createdAt()).isNull();
            assertThat(sellerInfo.updatedAt()).isNull();
        }

        @Test
        @DisplayName("null DTO를 입력하면 null을 반환합니다")
        void toCompositeResult_WithNullDto_ReturnsNull() {
            // given
            LegacyWebSellerQueryDto dto = null;

            // when
            SellerCompositeResult result = mapper.toCompositeResult(dto);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("toCompositeResult(Optional<LegacyWebSellerQueryDto>) 메서드 테스트")
    class ToCompositeResultFromOptionalTest {

        @Test
        @DisplayName("값이 있는 Optional을 SellerCompositeResult Optional로 변환합니다")
        void toCompositeResult_WithPresentOptional_ReturnsSellerCompositeResultOptional() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.defaultDto();
            Optional<LegacyWebSellerQueryDto> optionalDto = Optional.of(dto);

            // when
            Optional<SellerCompositeResult> result = mapper.toCompositeResult(optionalDto);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().seller().id()).isEqualTo(DEFAULT_SELLER_ID);
        }

        @Test
        @DisplayName("빈 Optional을 입력하면 빈 Optional을 반환합니다")
        void toCompositeResult_WithEmptyOptional_ReturnsEmptyOptional() {
            // given
            Optional<LegacyWebSellerQueryDto> optionalDto = Optional.empty();

            // when
            Optional<SellerCompositeResult> result = mapper.toCompositeResult(optionalDto);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("toAdminCompositeResult(LegacyWebSellerQueryDto) 메서드 테스트")
    class ToAdminCompositeResultFromDtoTest {

        @Test
        @DisplayName("정상적인 DTO를 SellerAdminCompositeResult로 변환합니다")
        void toAdminCompositeResult_WithValidDto_ReturnsSellerAdminCompositeResult() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.defaultDto();

            // when
            SellerAdminCompositeResult result = mapper.toAdminCompositeResult(dto);

            // then
            assertThat(result).isNotNull();

            SellerAdminCompositeResult.SellerInfo sellerInfo = result.seller();
            assertThat(sellerInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(sellerInfo.sellerName()).isEqualTo(DEFAULT_SELLER_NAME);

            SellerAdminCompositeResult.AddressInfo addressInfo = result.address();
            assertThat(addressInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(addressInfo.addressType()).isEqualTo("BUSINESS");

            SellerAdminCompositeResult.BusinessInfo businessInfo = result.businessInfo();
            assertThat(businessInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(businessInfo.registrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);

            SellerAdminCompositeResult.CsInfo csInfo = result.csInfo();
            assertThat(csInfo.id()).isEqualTo(DEFAULT_SELLER_ID);
            assertThat(csInfo.csPhone()).isEqualTo(DEFAULT_CS_NUMBER);

            SellerAdminCompositeResult.ContractInfo contractInfo = result.contractInfo();
            assertThat(contractInfo.id()).isNull();
            assertThat(contractInfo.commissionRate())
                    .isEqualByComparingTo(BigDecimal.valueOf(DEFAULT_COMMISSION_RATE));
            assertThat(contractInfo.contractStartDate()).isNull();
            assertThat(contractInfo.contractEndDate()).isNull();
            assertThat(contractInfo.status()).isNull();
            assertThat(contractInfo.specialTerms()).isNull();
            Instant expectedCreatedAt = DEFAULT_CREATED_AT.atZone(SEOUL_ZONE).toInstant();
            Instant expectedUpdatedAt = DEFAULT_UPDATED_AT.atZone(SEOUL_ZONE).toInstant();
            assertThat(contractInfo.createdAt()).isEqualTo(expectedCreatedAt);
            assertThat(contractInfo.updatedAt()).isEqualTo(expectedUpdatedAt);

            SellerAdminCompositeResult.SettlementInfo settlementInfo = result.settlementInfo();
            assertThat(settlementInfo.id()).isNull();
            assertThat(settlementInfo.bankCode()).isNull();
            assertThat(settlementInfo.bankName()).isEqualTo(DEFAULT_BANK_NAME);
            assertThat(settlementInfo.accountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
            assertThat(settlementInfo.accountHolderName()).isEqualTo(DEFAULT_ACCOUNT_HOLDER);
            assertThat(settlementInfo.settlementCycle()).isNull();
            assertThat(settlementInfo.settlementDay()).isNull();
            assertThat(settlementInfo.verified()).isFalse();
            assertThat(settlementInfo.verifiedAt()).isNull();
            assertThat(settlementInfo.createdAt()).isNull();
            assertThat(settlementInfo.updatedAt()).isNull();
        }

        @Test
        @DisplayName("commissionRate가 null일 때 null BigDecimal을 반환합니다")
        void toAdminCompositeResult_WithNullCommissionRate_ReturnsNullBigDecimal() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.withNullCommissionRate();

            // when
            SellerAdminCompositeResult result = mapper.toAdminCompositeResult(dto);

            // then
            assertThat(result).isNotNull();
            SellerAdminCompositeResult.ContractInfo contractInfo = result.contractInfo();
            assertThat(contractInfo.commissionRate()).isNull();
        }

        @Test
        @DisplayName("null DTO를 입력하면 null을 반환합니다")
        void toAdminCompositeResult_WithNullDto_ReturnsNull() {
            // given
            LegacyWebSellerQueryDto dto = null;

            // when
            SellerAdminCompositeResult result = mapper.toAdminCompositeResult(dto);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("toAdminCompositeResult(Optional<LegacyWebSellerQueryDto>) 메서드 테스트")
    class ToAdminCompositeResultFromOptionalTest {

        @Test
        @DisplayName("값이 있는 Optional을 SellerAdminCompositeResult Optional로 변환합니다")
        void
                toAdminCompositeResult_WithPresentOptional_ReturnsSellerAdminCompositeResultOptional() {
            // given
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.defaultDto();
            Optional<LegacyWebSellerQueryDto> optionalDto = Optional.of(dto);

            // when
            Optional<SellerAdminCompositeResult> result =
                    mapper.toAdminCompositeResult(optionalDto);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().seller().id()).isEqualTo(DEFAULT_SELLER_ID);
        }

        @Test
        @DisplayName("빈 Optional을 입력하면 빈 Optional을 반환합니다")
        void toAdminCompositeResult_WithEmptyOptional_ReturnsEmptyOptional() {
            // given
            Optional<LegacyWebSellerQueryDto> optionalDto = Optional.empty();

            // when
            Optional<SellerAdminCompositeResult> result =
                    mapper.toAdminCompositeResult(optionalDto);

            // then
            assertThat(result).isEmpty();
        }
    }
}
