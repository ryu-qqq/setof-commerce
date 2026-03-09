package com.ryuqq.setof.integration.test.repository.legacy.seller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import com.ryuqq.setof.storage.legacy.composite.seller.dto.LegacyWebSellerQueryDto;
import com.ryuqq.setof.storage.legacy.composite.seller.repository.LegacyWebSellerCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.seller.LegacySellerBusinessInfoEntityFixtures;
import com.ryuqq.setof.storage.legacy.seller.LegacySellerEntityFixtures;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerBusinessInfoEntity;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LegacyWebSellerCompositeQueryDslRepository 통합 테스트.
 *
 * <p>레거시 DB의 seller와 seller_business_info 테이블 JOIN 조회를 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.LEGACY)
@Tag(TestTags.SELLER)
@DisplayName("LegacyWebSellerCompositeQueryDslRepository 통합 테스트")
class LegacyWebSellerCompositeQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private LegacyWebSellerCompositeQueryDslRepository repository;

    @Nested
    @DisplayName("fetchSeller 메서드 테스트")
    class FetchSellerTest {

        @Test
        @DisplayName("셀러와 사업자 정보를 JOIN하여 조회합니다")
        void fetchSeller_WithExistingSeller_ReturnsSellerWithBusinessInfo() {
            // given
            Long sellerId = 100L;

            LegacySellerEntity seller =
                    LegacySellerEntityFixtures.builder()
                            .id(sellerId)
                            .sellerName("통합테스트 셀러")
                            .sellerLogoUrl("https://example.com/logo.png")
                            .sellerDescription("통합테스트 셀러 설명")
                            .commissionRate(15.0)
                            .build();

            LegacySellerBusinessInfoEntity businessInfo =
                    LegacySellerBusinessInfoEntityFixtures.builder()
                            .id(sellerId)
                            .registrationNumber("987-65-43210")
                            .companyName("통합테스트 회사")
                            .businessAddressLine1("서울시 서초구")
                            .businessAddressLine2("서초대로 123")
                            .businessAddressZipCode("06590")
                            .bankName("신한은행")
                            .accountNumber("987-654-321098")
                            .accountHolderName("통합테스트 회사")
                            .csNumber("1577-1234")
                            .csPhoneNumber("02-9876-5432")
                            .csEmail("cs@integration-test.com")
                            .saleReportNumber("2024-서울서초-98765")
                            .representative("김테스트")
                            .build();

            persist(seller);
            persist(businessInfo);
            flushAndClear();

            // when
            Optional<LegacyWebSellerQueryDto> result = repository.fetchSeller(sellerId);

            // then
            assertThat(result).isPresent();

            LegacyWebSellerQueryDto dto = result.get();
            assertThat(dto.sellerId()).isEqualTo(sellerId);
            assertThat(dto.sellerName()).isEqualTo("통합테스트 셀러");
            assertThat(dto.companyName()).isEqualTo("통합테스트 회사");
            assertThat(dto.logoUrl()).isEqualTo("https://example.com/logo.png");
            assertThat(dto.sellerDescription()).isEqualTo("통합테스트 셀러 설명");
            assertThat(dto.commissionRate()).isEqualTo(15.0);
            assertThat(dto.businessAddressLine1()).isEqualTo("서울시 서초구");
            assertThat(dto.businessAddressLine2()).isEqualTo("서초대로 123");
            assertThat(dto.businessAddressZipCode()).isEqualTo("06590");
            assertThat(dto.csNumber()).isEqualTo("1577-1234");
            assertThat(dto.csPhoneNumber()).isEqualTo("02-9876-5432");
            assertThat(dto.csEmail()).isEqualTo("cs@integration-test.com");
            assertThat(dto.registrationNumber()).isEqualTo("987-65-43210");
            assertThat(dto.saleReportNumber()).isEqualTo("2024-서울서초-98765");
            assertThat(dto.representative()).isEqualTo("김테스트");
            assertThat(dto.bankName()).isEqualTo("신한은행");
            assertThat(dto.accountNumber()).isEqualTo("987-654-321098");
            assertThat(dto.accountHolderName()).isEqualTo("통합테스트 회사");
        }

        @Test
        @DisplayName("존재하지 않는 셀러 ID로 조회 시 빈 Optional을 반환합니다")
        void fetchSeller_WithNonExistingSeller_ReturnsEmptyOptional() {
            // given
            Long nonExistingSellerId = 999999L;

            // when
            Optional<LegacyWebSellerQueryDto> result = repository.fetchSeller(nonExistingSellerId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("seller만 있고 business_info가 없으면 JOIN 실패로 빈 Optional을 반환합니다")
        void fetchSeller_WithSellerOnlyNoBusinessInfo_ReturnsEmptyOptional() {
            // given
            Long sellerId = 200L;

            LegacySellerEntity seller =
                    LegacySellerEntityFixtures.builder()
                            .id(sellerId)
                            .sellerName("사업자정보없는셀러")
                            .build();

            persist(seller);
            flushAndClear();

            // when
            Optional<LegacyWebSellerQueryDto> result = repository.fetchSeller(sellerId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByRegistrationNumber 메서드 테스트")
    class ExistsByRegistrationNumberTest {

        @Test
        @DisplayName("존재하는 사업자등록번호로 조회 시 true를 반환합니다")
        void existsByRegistrationNumber_WithExistingNumber_ReturnsTrue() {
            // given
            Long sellerId = 300L;
            String registrationNumber = "111-22-33344";

            LegacySellerEntity seller = LegacySellerEntityFixtures.builder().id(sellerId).build();

            LegacySellerBusinessInfoEntity businessInfo =
                    LegacySellerBusinessInfoEntityFixtures.builder()
                            .id(sellerId)
                            .registrationNumber(registrationNumber)
                            .build();

            persist(seller);
            persist(businessInfo);
            flushAndClear();

            // when
            boolean result = repository.existsByRegistrationNumber(registrationNumber);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 사업자등록번호로 조회 시 false를 반환합니다")
        void existsByRegistrationNumber_WithNonExistingNumber_ReturnsFalse() {
            // given
            String nonExistingNumber = "999-88-77766";

            // when
            boolean result = repository.existsByRegistrationNumber(nonExistingNumber);

            // then
            assertThat(result).isFalse();
        }
    }
}
