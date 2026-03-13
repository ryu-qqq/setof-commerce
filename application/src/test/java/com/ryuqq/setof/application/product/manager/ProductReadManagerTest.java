package com.ryuqq.setof.application.product.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.product.port.out.query.ProductQueryPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.exception.ProductNotFoundException;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.setof.commerce.domain.product.ProductFixtures;
import java.util.Collections;
import java.util.List;
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
@DisplayName("ProductReadManager 단위 테스트")
class ProductReadManagerTest {

    @InjectMocks private ProductReadManager sut;

    @Mock private ProductQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 상품 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 상품을 반환한다")
        void getById_ExistingProduct_ReturnsProduct() {
            // given
            ProductId id = ProductId.of(1L);
            Product expected = ProductFixtures.activeProduct(1L);

            given(queryPort.findById(id)).willReturn(Optional.of(expected));

            // when
            Product result = sut.getById(id);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ProductNotFoundException이 발생한다")
        void getById_NonExistingProduct_ThrowsProductNotFoundException() {
            // given
            ProductId id = ProductId.of(999L);

            given(queryPort.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(id)).isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByProductGroupId() - 상품그룹 ID로 상품 목록 조회")
    class FindByProductGroupIdTest {

        @Test
        @DisplayName("상품그룹 ID로 상품 목록을 반환한다")
        void findByProductGroupId_ValidId_ReturnsProducts() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);
            List<Product> expected =
                    List.of(ProductFixtures.activeProduct(1L), ProductFixtures.activeProduct(2L));

            given(queryPort.findByProductGroupId(pgId)).willReturn(expected);

            // when
            List<Product> result = sut.findByProductGroupId(pgId);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByProductGroupId(pgId);
        }

        @Test
        @DisplayName("상품이 없는 상품그룹 ID로 조회하면 빈 목록을 반환한다")
        void findByProductGroupId_NoProducts_ReturnsEmptyList() {
            // given
            ProductGroupId pgId = ProductGroupId.of(999L);

            given(queryPort.findByProductGroupId(pgId)).willReturn(Collections.emptyList());

            // when
            List<Product> result = sut.findByProductGroupId(pgId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getByIds() - ID 목록으로 상품 목록 조회")
    class GetByIdsTest {

        @Test
        @DisplayName("ID 목록으로 상품 목록을 반환한다")
        void getByIds_ValidIds_ReturnsProducts() {
            // given
            List<ProductId> ids = List.of(ProductId.of(1L), ProductId.of(2L));
            List<Product> expected =
                    List.of(ProductFixtures.activeProduct(1L), ProductFixtures.activeProduct(2L));

            given(queryPort.findByIds(ids)).willReturn(expected);

            // when
            List<Product> result = sut.getByIds(ids);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByIds(ids);
        }
    }
}
