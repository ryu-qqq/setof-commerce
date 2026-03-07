package com.ryuqq.setof.application.seller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.port.out.query.SellerBusinessInfoQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.exception.BusinessInfoNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerBusinessInfoReadManager 단위 테스트")
class SellerBusinessInfoReadManagerTest {

    @InjectMocks private SellerBusinessInfoReadManager sut;

    @Mock private SellerBusinessInfoQueryPort sellerBusinessInfoQueryPort;

    @Nested
    @DisplayName("getBySellerId() - 셀러 ID로 사업자 정보 조회")
    class GetBySellerIdTest {

        @Test
        @DisplayName("존재하는 셀러의 사업자 정보를 조회한다")
        void getBySellerId_ExistingBusinessInfo_ReturnsBusinessInfo() {
            // given
            SellerId sellerId = SellerId.of(1L);
            SellerBusinessInfo expected = SellerDomainFixtures.sellerBusinessInfo(1L);

            given(sellerBusinessInfoQueryPort.findBySellerId(sellerId))
                    .willReturn(Optional.of(expected));

            // when
            SellerBusinessInfo result = sut.getBySellerId(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerBusinessInfoQueryPort).should().findBySellerId(sellerId);
        }

        @Test
        @DisplayName("사업자 정보가 없으면 예외가 발생한다")
        void getBySellerId_NonExistingBusinessInfo_ThrowsException() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerBusinessInfoQueryPort.findBySellerId(sellerId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getBySellerId(sellerId))
                    .isInstanceOf(BusinessInfoNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("existsBySellerId() - 셀러 ID로 사업자 정보 존재 여부 확인")
    class ExistsBySellerIdTest {

        @Test
        @DisplayName("사업자 정보가 존재하면 true를 반환한다")
        void existsBySellerId_ExistingBusinessInfo_ReturnsTrue() {
            // given
            SellerId sellerId = SellerId.of(1L);

            given(sellerBusinessInfoQueryPort.existsBySellerId(sellerId)).willReturn(true);

            // when
            boolean result = sut.existsBySellerId(sellerId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("사업자 정보가 없으면 false를 반환한다")
        void existsBySellerId_NonExistingBusinessInfo_ReturnsFalse() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerBusinessInfoQueryPort.existsBySellerId(sellerId)).willReturn(false);

            // when
            boolean result = sut.existsBySellerId(sellerId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByRegistrationNumber() - 사업자등록번호 존재 여부 확인")
    class ExistsByRegistrationNumberTest {

        @Test
        @DisplayName("이미 사용 중인 사업자등록번호이면 true를 반환한다")
        void existsByRegistrationNumber_DuplicateNumber_ReturnsTrue() {
            // given
            String registrationNumber = "123-45-67890";

            given(sellerBusinessInfoQueryPort.existsByRegistrationNumber(registrationNumber))
                    .willReturn(true);

            // when
            boolean result = sut.existsByRegistrationNumber(registrationNumber);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("사용 가능한 사업자등록번호이면 false를 반환한다")
        void existsByRegistrationNumber_UniqueNumber_ReturnsFalse() {
            // given
            String registrationNumber = "987-65-43210";

            given(sellerBusinessInfoQueryPort.existsByRegistrationNumber(registrationNumber))
                    .willReturn(false);

            // when
            boolean result = sut.existsByRegistrationNumber(registrationNumber);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByRegistrationNumberExcluding() - 특정 셀러 제외한 사업자등록번호 존재 여부 확인")
    class ExistsByRegistrationNumberExcludingTest {

        @Test
        @DisplayName("다른 셀러가 동일한 사업자등록번호를 사용 중이면 true를 반환한다")
        void existsByRegistrationNumberExcluding_OtherSellerUsingSameNumber_ReturnsTrue() {
            // given
            String registrationNumber = "123-45-67890";
            SellerId excludeId = SellerId.of(1L);

            given(
                            sellerBusinessInfoQueryPort.existsByRegistrationNumberExcluding(
                                    registrationNumber, excludeId))
                    .willReturn(true);

            // when
            boolean result = sut.existsByRegistrationNumberExcluding(registrationNumber, excludeId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("다른 셀러가 동일한 사업자등록번호를 사용하지 않으면 false를 반환한다")
        void existsByRegistrationNumberExcluding_NoOtherSellerUsingSameNumber_ReturnsFalse() {
            // given
            String registrationNumber = "999-99-99999";
            SellerId excludeId = SellerId.of(1L);

            given(
                            sellerBusinessInfoQueryPort.existsByRegistrationNumberExcluding(
                                    registrationNumber, excludeId))
                    .willReturn(false);

            // when
            boolean result = sut.existsByRegistrationNumberExcluding(registrationNumber, excludeId);

            // then
            assertThat(result).isFalse();
        }
    }
}
