package com.ryuqq.setof.application.seller.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.manager.SellerBusinessInfoReadManager;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.exception.BusinessInfoNotFoundException;
import com.ryuqq.setof.domain.seller.exception.RegistrationNumberDuplicateException;
import com.ryuqq.setof.domain.seller.id.SellerId;
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
@DisplayName("SellerBusinessInfoValidator 단위 테스트")
class SellerBusinessInfoValidatorTest {

    @InjectMocks private SellerBusinessInfoValidator sut;

    @Mock private SellerBusinessInfoReadManager sellerBusinessInfoReadManager;

    @Nested
    @DisplayName("findExistingOrThrow() - 사업자 정보 조회 또는 예외 발생")
    class FindExistingOrThrowTest {

        @Test
        @DisplayName("존재하는 사업자 정보를 반환한다")
        void findExistingOrThrow_ExistingBusinessInfo_ReturnsBusinessInfo() {
            // given
            SellerId sellerId = SellerId.of(1L);
            SellerBusinessInfo expected = SellerDomainFixtures.sellerBusinessInfo(1L);

            given(sellerBusinessInfoReadManager.getBySellerId(sellerId)).willReturn(expected);

            // when
            SellerBusinessInfo result = sut.findExistingOrThrow(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerBusinessInfoReadManager).should().getBySellerId(sellerId);
        }

        @Test
        @DisplayName("사업자 정보가 없으면 예외가 발생한다")
        void findExistingOrThrow_NonExistingBusinessInfo_ThrowsException() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerBusinessInfoReadManager.getBySellerId(sellerId))
                    .willThrow(new BusinessInfoNotFoundException());

            // when & then
            assertThatThrownBy(() -> sut.findExistingOrThrow(sellerId))
                    .isInstanceOf(BusinessInfoNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("validateRegistrationNumberNotDuplicate() - 사업자등록번호 중복 검사")
    class ValidateRegistrationNumberNotDuplicateTest {

        @Test
        @DisplayName("사용 가능한 사업자등록번호이면 예외가 발생하지 않는다")
        void validateRegistrationNumberNotDuplicate_UniqueNumber_NoException() {
            // given
            String registrationNumber = "123-45-67890";

            given(sellerBusinessInfoReadManager.existsByRegistrationNumber(registrationNumber))
                    .willReturn(false);

            // when & then (no exception thrown)
            sut.validateRegistrationNumberNotDuplicate(registrationNumber);

            then(sellerBusinessInfoReadManager)
                    .should()
                    .existsByRegistrationNumber(registrationNumber);
        }

        @Test
        @DisplayName("이미 사용 중인 사업자등록번호이면 예외가 발생한다")
        void validateRegistrationNumberNotDuplicate_DuplicateNumber_ThrowsException() {
            // given
            String registrationNumber = "123-45-67890";

            given(sellerBusinessInfoReadManager.existsByRegistrationNumber(registrationNumber))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> sut.validateRegistrationNumberNotDuplicate(registrationNumber))
                    .isInstanceOf(RegistrationNumberDuplicateException.class);
        }
    }

    @Nested
    @DisplayName("validateRegistrationNumberNotDuplicateExcluding() - 특정 셀러 제외 사업자등록번호 중복 검사")
    class ValidateRegistrationNumberNotDuplicateExcludingTest {

        @Test
        @DisplayName("자신을 제외하고 사용 가능한 사업자등록번호이면 예외가 발생하지 않는다")
        void
                validateRegistrationNumberNotDuplicateExcluding_UniqueNumberExcludingSelf_NoException() {
            // given
            String registrationNumber = "123-45-67890";
            SellerId excludeId = SellerId.of(1L);

            given(
                            sellerBusinessInfoReadManager.existsByRegistrationNumberExcluding(
                                    registrationNumber, excludeId))
                    .willReturn(false);

            // when & then (no exception thrown)
            sut.validateRegistrationNumberNotDuplicateExcluding(registrationNumber, excludeId);

            then(sellerBusinessInfoReadManager)
                    .should()
                    .existsByRegistrationNumberExcluding(registrationNumber, excludeId);
        }

        @Test
        @DisplayName("자신을 제외하고 중복된 사업자등록번호이면 예외가 발생한다")
        void
                validateRegistrationNumberNotDuplicateExcluding_DuplicateNumberExcludingSelf_ThrowsException() {
            // given
            String registrationNumber = "123-45-67890";
            SellerId excludeId = SellerId.of(1L);

            given(
                            sellerBusinessInfoReadManager.existsByRegistrationNumberExcluding(
                                    registrationNumber, excludeId))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(
                            () ->
                                    sut.validateRegistrationNumberNotDuplicateExcluding(
                                            registrationNumber, excludeId))
                    .isInstanceOf(RegistrationNumberDuplicateException.class);
        }
    }
}
