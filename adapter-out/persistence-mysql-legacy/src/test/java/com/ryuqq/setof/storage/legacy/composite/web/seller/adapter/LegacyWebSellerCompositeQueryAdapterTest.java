package com.ryuqq.setof.storage.legacy.composite.web.seller.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.application.seller.dto.composite.SellerAdminCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import com.ryuqq.setof.domain.legacy.seller.dto.query.LegacySellerSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.seller.LegacyWebSellerQueryDtoFixtures;
import com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.seller.mapper.LegacyWebSellerMapper;
import com.ryuqq.setof.storage.legacy.composite.web.seller.repository.LegacyWebSellerCompositeQueryDslRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyWebSellerCompositeQueryAdapter 단위 테스트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyWebSellerCompositeQueryAdapter 단위 테스트")
class LegacyWebSellerCompositeQueryAdapterTest {

    @Mock private LegacyWebSellerCompositeQueryDslRepository repository;

    @Mock private LegacyWebSellerMapper mapper;

    @InjectMocks private LegacyWebSellerCompositeQueryAdapter adapter;

    @Nested
    @DisplayName("findSellerCompositeById 메서드 테스트")
    class FindSellerCompositeByIdTest {

        @Test
        @DisplayName("판매자가 존재할 때 SellerCompositeResult를 반환합니다")
        void findSellerCompositeById_WithExistingSeller_ReturnsSellerCompositeResult() {
            // given
            Long sellerId = 1L;
            LegacySellerSearchCondition condition =
                    LegacySellerSearchCondition.ofSellerId(sellerId);
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.defaultDto();
            SellerCompositeResult expectedResult =
                    new SellerCompositeResult(null, null, null, null);

            given(repository.fetchSeller(condition)).willReturn(Optional.of(dto));
            given(mapper.toCompositeResult(Optional.of(dto)))
                    .willReturn(Optional.of(expectedResult));

            // when
            Optional<SellerCompositeResult> result = adapter.findSellerCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedResult);
            then(repository).should(times(1)).fetchSeller(condition);
            then(mapper).should(times(1)).toCompositeResult(Optional.of(dto));
        }

        @Test
        @DisplayName("판매자가 존재하지 않을 때 빈 Optional을 반환합니다")
        void findSellerCompositeById_WithNonExistingSeller_ReturnsEmptyOptional() {
            // given
            Long sellerId = 999L;
            LegacySellerSearchCondition condition =
                    LegacySellerSearchCondition.ofSellerId(sellerId);

            given(repository.fetchSeller(condition)).willReturn(Optional.empty());
            given(mapper.toCompositeResult(Optional.empty())).willReturn(Optional.empty());

            // when
            Optional<SellerCompositeResult> result = adapter.findSellerCompositeById(sellerId);

            // then
            assertThat(result).isEmpty();
            then(repository).should(times(1)).fetchSeller(condition);
            then(mapper).should(times(1)).toCompositeResult(Optional.empty());
        }
    }

    @Nested
    @DisplayName("findAdminCompositeById 메서드 테스트")
    class FindAdminCompositeByIdTest {

        @Test
        @DisplayName("판매자가 존재할 때 SellerAdminCompositeResult를 반환합니다")
        void findAdminCompositeById_WithExistingSeller_ReturnsSellerAdminCompositeResult() {
            // given
            Long sellerId = 1L;
            LegacySellerSearchCondition condition =
                    LegacySellerSearchCondition.ofSellerId(sellerId);
            LegacyWebSellerQueryDto dto = LegacyWebSellerQueryDtoFixtures.defaultDto();
            SellerAdminCompositeResult expectedResult =
                    new SellerAdminCompositeResult(null, null, null, null, null, null);

            given(repository.fetchSeller(condition)).willReturn(Optional.of(dto));
            given(mapper.toAdminCompositeResult(Optional.of(dto)))
                    .willReturn(Optional.of(expectedResult));

            // when
            Optional<SellerAdminCompositeResult> result = adapter.findAdminCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedResult);
            then(repository).should(times(1)).fetchSeller(condition);
            then(mapper).should(times(1)).toAdminCompositeResult(Optional.of(dto));
        }

        @Test
        @DisplayName("판매자가 존재하지 않을 때 빈 Optional을 반환합니다")
        void findAdminCompositeById_WithNonExistingSeller_ReturnsEmptyOptional() {
            // given
            Long sellerId = 999L;
            LegacySellerSearchCondition condition =
                    LegacySellerSearchCondition.ofSellerId(sellerId);

            given(repository.fetchSeller(condition)).willReturn(Optional.empty());
            given(mapper.toAdminCompositeResult(Optional.empty())).willReturn(Optional.empty());

            // when
            Optional<SellerAdminCompositeResult> result = adapter.findAdminCompositeById(sellerId);

            // then
            assertThat(result).isEmpty();
            then(repository).should(times(1)).fetchSeller(condition);
            then(mapper).should(times(1)).toAdminCompositeResult(Optional.empty());
        }
    }

    @Nested
    @DisplayName("findPolicyCompositeById 메서드 테스트")
    class FindPolicyCompositeByIdTest {

        @Test
        @DisplayName("레거시 DB에는 정책 테이블이 없으므로 항상 빈 Optional을 반환합니다")
        void findPolicyCompositeById_Always_ReturnsEmptyOptional() {
            // given
            Long sellerId = 1L;

            // when
            Optional<SellerPolicyCompositeResult> result =
                    adapter.findPolicyCompositeById(sellerId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByRegistrationNumber 메서드 테스트")
    class ExistsByRegistrationNumberTest {

        @Test
        @DisplayName("사업자등록번호가 존재할 때 true를 반환합니다")
        void existsByRegistrationNumber_WithExistingNumber_ReturnsTrue() {
            // given
            String registrationNumber = "123-45-67890";
            given(repository.existsByRegistrationNumber(registrationNumber)).willReturn(true);

            // when
            boolean result = adapter.existsByRegistrationNumber(registrationNumber);

            // then
            assertThat(result).isTrue();
            then(repository).should(times(1)).existsByRegistrationNumber(registrationNumber);
        }

        @Test
        @DisplayName("사업자등록번호가 존재하지 않을 때 false를 반환합니다")
        void existsByRegistrationNumber_WithNonExistingNumber_ReturnsFalse() {
            // given
            String registrationNumber = "999-99-99999";
            given(repository.existsByRegistrationNumber(registrationNumber)).willReturn(false);

            // when
            boolean result = adapter.existsByRegistrationNumber(registrationNumber);

            // then
            assertThat(result).isFalse();
            then(repository).should(times(1)).existsByRegistrationNumber(registrationNumber);
        }
    }
}
