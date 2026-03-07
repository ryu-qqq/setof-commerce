package com.ryuqq.setof.application.seller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.port.out.query.SellerCsQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.exception.SellerCsNotFoundException;
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
@DisplayName("SellerCsReadManager 단위 테스트")
class SellerCsReadManagerTest {

    @InjectMocks private SellerCsReadManager sut;

    @Mock private SellerCsQueryPort sellerCsQueryPort;

    @Nested
    @DisplayName("getBySellerId() - 셀러 ID로 CS 정보 조회")
    class GetBySellerIdTest {

        @Test
        @DisplayName("존재하는 셀러의 CS 정보를 조회한다")
        void getBySellerId_ExistingCs_ReturnsCs() {
            // given
            SellerId sellerId = SellerId.of(1L);
            SellerCs expected = SellerDomainFixtures.sellerCs(1L);

            given(sellerCsQueryPort.findBySellerId(sellerId)).willReturn(Optional.of(expected));

            // when
            SellerCs result = sut.getBySellerId(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerCsQueryPort).should().findBySellerId(sellerId);
        }

        @Test
        @DisplayName("CS 정보가 없으면 예외가 발생한다")
        void getBySellerId_NonExistingCs_ThrowsException() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerCsQueryPort.findBySellerId(sellerId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getBySellerId(sellerId))
                    .isInstanceOf(SellerCsNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("existsBySellerId() - 셀러 ID로 CS 정보 존재 여부 확인")
    class ExistsBySellerIdTest {

        @Test
        @DisplayName("CS 정보가 존재하면 true를 반환한다")
        void existsBySellerId_ExistingCs_ReturnsTrue() {
            // given
            SellerId sellerId = SellerId.of(1L);

            given(sellerCsQueryPort.existsBySellerId(sellerId)).willReturn(true);

            // when
            boolean result = sut.existsBySellerId(sellerId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("CS 정보가 없으면 false를 반환한다")
        void existsBySellerId_NonExistingCs_ReturnsFalse() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerCsQueryPort.existsBySellerId(sellerId)).willReturn(false);

            // when
            boolean result = sut.existsBySellerId(sellerId);

            // then
            assertThat(result).isFalse();
        }
    }
}
