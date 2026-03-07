package com.ryuqq.setof.application.seller.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerQueryFixtures;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.manager.SellerCompositionReadManager;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
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
@DisplayName("GetSellerForAdminService 단위 테스트")
class GetSellerForAdminServiceTest {

    @InjectMocks private GetSellerForAdminService sut;

    @Mock private SellerCompositionReadManager sellerCompositionReadManager;

    @Nested
    @DisplayName("execute() - 관리자용 셀러 조회")
    class ExecuteTest {

        @Test
        @DisplayName("존재하는 셀러를 조회하면 SellerCompositeResult를 반환한다")
        void execute_ExistingSeller_ReturnsCompositeResult() {
            // given
            Long sellerId = 1L;
            SellerCompositeResult expected = SellerQueryFixtures.sellerCompositeResult(sellerId);

            given(sellerCompositionReadManager.getSellerComposite(sellerId)).willReturn(expected);

            // when
            SellerCompositeResult result = sut.execute(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.seller().id()).isEqualTo(sellerId);
            then(sellerCompositionReadManager).should().getSellerComposite(sellerId);
        }

        @Test
        @DisplayName("존재하지 않는 셀러 조회 시 예외가 발생한다")
        void execute_NonExistingSeller_ThrowsException() {
            // given
            Long sellerId = 999L;

            given(sellerCompositionReadManager.getSellerComposite(sellerId))
                    .willThrow(new SellerNotFoundException(sellerId));

            // when & then
            assertThatThrownBy(() -> sut.execute(sellerId))
                    .isInstanceOf(SellerNotFoundException.class);
        }
    }
}
