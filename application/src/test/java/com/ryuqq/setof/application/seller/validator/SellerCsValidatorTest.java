package com.ryuqq.setof.application.seller.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.manager.SellerCsReadManager;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.exception.SellerCsNotFoundException;
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
@DisplayName("SellerCsValidator 단위 테스트")
class SellerCsValidatorTest {

    @InjectMocks private SellerCsValidator sut;

    @Mock private SellerCsReadManager sellerCsReadManager;

    @Nested
    @DisplayName("findExistingOrThrow() - CS 정보 조회 또는 예외 발생")
    class FindExistingOrThrowTest {

        @Test
        @DisplayName("존재하는 CS 정보를 반환한다")
        void findExistingOrThrow_ExistingCs_ReturnsCs() {
            // given
            SellerId sellerId = SellerId.of(1L);
            SellerCs expected = SellerDomainFixtures.sellerCs(1L);

            given(sellerCsReadManager.getBySellerId(sellerId)).willReturn(expected);

            // when
            SellerCs result = sut.findExistingOrThrow(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerCsReadManager).should().getBySellerId(sellerId);
        }

        @Test
        @DisplayName("CS 정보가 없으면 예외가 발생한다")
        void findExistingOrThrow_NonExistingCs_ThrowsException() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerCsReadManager.getBySellerId(sellerId))
                    .willThrow(new SellerCsNotFoundException(sellerId.value()));

            // when & then
            assertThatThrownBy(() -> sut.findExistingOrThrow(sellerId))
                    .isInstanceOf(SellerCsNotFoundException.class);
        }
    }
}
