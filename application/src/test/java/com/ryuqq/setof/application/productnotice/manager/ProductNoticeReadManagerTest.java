package com.ryuqq.setof.application.productnotice.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.exception.ProductNoticeNotFoundException;
import com.setof.commerce.domain.productnotice.ProductNoticeFixtures;
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
@DisplayName("ProductNoticeReadManager 단위 테스트")
class ProductNoticeReadManagerTest {

    @InjectMocks private ProductNoticeReadManager sut;

    @Mock private ProductNoticeQueryPort queryPort;

    @Nested
    @DisplayName("findByProductGroupId() - ProductGroupId로 조회")
    class FindByProductGroupIdTest {

        @Test
        @DisplayName("존재하는 ProductGroupId로 ProductNotice를 조회한다")
        void findByProductGroupId_Exists_ReturnsOptionalNotice() {
            // given
            ProductGroupId productGroupId =
                    ProductGroupId.of(ProductNoticeFixtures.DEFAULT_PRODUCT_GROUP_ID);
            ProductNotice expected = ProductNoticeFixtures.activeNotice();

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(Optional.of(expected));

            // when
            Optional<ProductNotice> result = sut.findByProductGroupId(productGroupId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(queryPort).should().findByProductGroupId(productGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ProductGroupId로 조회하면 빈 Optional을 반환한다")
        void findByProductGroupId_NotExists_ReturnsEmpty() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(999L);

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(Optional.empty());

            // when
            Optional<ProductNotice> result = sut.findByProductGroupId(productGroupId);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findByProductGroupId(productGroupId);
        }
    }

    @Nested
    @DisplayName("getByProductGroupId() - ProductGroupId로 단건 조회 (예외 발생)")
    class GetByProductGroupIdTest {

        @Test
        @DisplayName("존재하는 ProductGroupId로 ProductNotice를 반환한다")
        void getByProductGroupId_Exists_ReturnsNotice() {
            // given
            ProductGroupId productGroupId =
                    ProductGroupId.of(ProductNoticeFixtures.DEFAULT_PRODUCT_GROUP_ID);
            ProductNotice expected = ProductNoticeFixtures.activeNotice();

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(Optional.of(expected));

            // when
            ProductNotice result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findByProductGroupId(productGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ProductGroupId로 조회 시 ProductNoticeNotFoundException이 발생한다")
        void getByProductGroupId_NotExists_ThrowsNotFoundException() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(999L);

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getByProductGroupId(productGroupId))
                    .isInstanceOf(ProductNoticeNotFoundException.class);
        }
    }
}
