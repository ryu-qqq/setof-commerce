package com.ryuqq.setof.application.seller.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.validator.SellerBusinessInfoValidator;
import com.ryuqq.setof.application.seller.validator.SellerValidator;
import com.ryuqq.setof.domain.seller.exception.RegistrationNumberDuplicateException;
import com.ryuqq.setof.domain.seller.exception.SellerNameDuplicateException;
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
@DisplayName("SellerRegistrationCoordinator 단위 테스트")
class SellerRegistrationCoordinatorTest {

    @InjectMocks private SellerRegistrationCoordinator sut;

    @Mock private SellerValidator sellerValidator;
    @Mock private SellerBusinessInfoValidator sellerBusinessInfoValidator;
    @Mock private SellerCommandFacade sellerCommandFacade;
    @Mock private SellerRegistrationBundle bundle;

    @Nested
    @DisplayName("register() - 셀러 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 번들로 셀러를 등록하고 셀러 ID를 반환한다")
        void register_ValidBundle_ReturnsSellerId() {
            // given
            Long expectedSellerId = 1L;
            String sellerName = "테스트셀러";
            String registrationNumber = "123-45-67890";

            given(bundle.sellerNameValue()).willReturn(sellerName);
            given(bundle.registrationNumberValue()).willReturn(registrationNumber);
            willDoNothing().given(sellerValidator).validateSellerNameNotDuplicate(sellerName);
            willDoNothing()
                    .given(sellerBusinessInfoValidator)
                    .validateRegistrationNumberNotDuplicate(registrationNumber);
            given(sellerCommandFacade.registerSeller(bundle)).willReturn(expectedSellerId);

            // when
            Long result = sut.register(bundle);

            // then
            assertThat(result).isEqualTo(expectedSellerId);
            then(sellerValidator).should().validateSellerNameNotDuplicate(sellerName);
            then(sellerBusinessInfoValidator)
                    .should()
                    .validateRegistrationNumberNotDuplicate(registrationNumber);
            then(sellerCommandFacade).should().registerSeller(bundle);
        }

        @Test
        @DisplayName("셀러명이 중복이면 예외가 발생한다")
        void register_DuplicateSellerName_ThrowsException() {
            // given
            String sellerName = "중복셀러";

            given(bundle.sellerNameValue()).willReturn(sellerName);
            willThrow(new SellerNameDuplicateException(sellerName))
                    .given(sellerValidator)
                    .validateSellerNameNotDuplicate(sellerName);

            // when & then
            assertThatThrownBy(() -> sut.register(bundle))
                    .isInstanceOf(SellerNameDuplicateException.class);

            then(sellerCommandFacade).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("사업자등록번호가 중복이면 예외가 발생한다")
        void register_DuplicateRegistrationNumber_ThrowsException() {
            // given
            String sellerName = "테스트셀러";
            String registrationNumber = "123-45-67890";

            given(bundle.sellerNameValue()).willReturn(sellerName);
            given(bundle.registrationNumberValue()).willReturn(registrationNumber);
            willDoNothing().given(sellerValidator).validateSellerNameNotDuplicate(sellerName);
            willThrow(new RegistrationNumberDuplicateException(registrationNumber))
                    .given(sellerBusinessInfoValidator)
                    .validateRegistrationNumberNotDuplicate(registrationNumber);

            // when & then
            assertThatThrownBy(() -> sut.register(bundle))
                    .isInstanceOf(RegistrationNumberDuplicateException.class);

            then(sellerCommandFacade).shouldHaveNoInteractions();
        }
    }
}
