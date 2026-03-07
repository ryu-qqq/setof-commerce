package com.ryuqq.setof.application.seller.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.seller.SellerCommandFixtures;
import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import java.time.Instant;
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
@DisplayName("SellerCommandFactory 단위 테스트")
class SellerCommandFactoryTest {

    @InjectMocks private SellerCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("createRegistrationBundle() - Command → Bundle 변환")
    class CreateRegistrationBundleTest {

        @Test
        @DisplayName("RegisterSellerCommand를 SellerRegistrationBundle로 변환한다")
        void createRegistrationBundle_ValidCommand_ReturnsBundle() {
            // given
            RegisterSellerCommand command = SellerCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            SellerRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.seller()).isNotNull();
            assertThat(bundle.businessInfo()).isNotNull();
        }

        @Test
        @DisplayName("커맨드의 셀러명이 Bundle 내 Seller에 정확히 반영된다")
        void createRegistrationBundle_SellerNameReflected_InBundle() {
            // given
            RegisterSellerCommand command = SellerCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            SellerRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.sellerNameValue()).isEqualTo(command.seller().sellerName());
        }

        @Test
        @DisplayName("커맨드의 사업자등록번호가 Bundle 내 BusinessInfo에 정확히 반영된다")
        void createRegistrationBundle_RegistrationNumberReflected_InBundle() {
            // given
            RegisterSellerCommand command = SellerCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            SellerRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.registrationNumberValue())
                    .isEqualTo(command.businessInfo().registrationNumber());
        }
    }
}
