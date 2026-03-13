package com.ryuqq.setof.application.product.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.product.manager.ProductCommandManager;
import com.ryuqq.setof.application.product.manager.ProductOptionMappingCommandManager;
import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.setof.commerce.domain.product.ProductFixtures;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
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
@DisplayName("ProductCommandCoordinator 단위 테스트")
class ProductCommandCoordinatorTest {

    @InjectMocks private ProductCommandCoordinator sut;

    @Mock private ProductCommandManager productCommandManager;
    @Mock private ProductOptionMappingCommandManager optionMappingCommandManager;
    @Mock private ProductReadManager productReadManager;

    @Nested
    @DisplayName("register() - 상품 등록")
    class RegisterTest {

        @Test
        @DisplayName("상품 목록을 등록하고 저장된 ID 목록을 반환한다")
        void register_ValidProducts_ReturnsSavedIds() {
            // given
            List<Product> products =
                    List.of(ProductFixtures.activeProduct(1L), ProductFixtures.activeProduct(2L));
            List<Long> expectedIds = List.of(1L, 2L);

            given(productCommandManager.persistAll(products)).willReturn(expectedIds);

            // when
            List<Long> result = sut.register(products);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(1L, 2L);
            then(productCommandManager).should().persistAll(products);
        }

        @Test
        @DisplayName("각 상품에 대해 옵션 매핑이 저장된다")
        void register_ValidProducts_PersistsOptionMappingsForEachProduct() {
            // given
            List<Product> products = List.of(ProductFixtures.activeProduct(1L));
            List<Long> savedIds = List.of(10L);

            given(productCommandManager.persistAll(products)).willReturn(savedIds);

            // when
            sut.register(products);

            // then
            then(optionMappingCommandManager).should().persistAllForProduct(eq(10L), anyList());
        }

        private Long eq(Long val) {
            return org.mockito.ArgumentMatchers.eq(val);
        }

        @Test
        @DisplayName("빈 상품 목록으로 등록하면 빈 ID 목록을 반환한다")
        void register_EmptyProducts_ReturnsEmptyIds() {
            // given
            List<Product> products = Collections.emptyList();
            given(productCommandManager.persistAll(products)).willReturn(Collections.emptyList());

            // when
            List<Long> result = sut.register(products);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("update() - 상품 Diff 기반 수정")
    class UpdateTest {

        @Test
        @DisplayName("상품그룹 ID와 수정 데이터로 diff 기반 수정을 수행한다")
        void update_ValidUpdateData_PerformsDiffUpdate() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");
            ProductUpdateData updateData = ProductFixtures.defaultProductUpdateData(1L);
            List<Product> existingProducts = List.of(ProductFixtures.activeProduct(1L));

            given(productReadManager.findByProductGroupId(pgId)).willReturn(existingProducts);
            given(productCommandManager.persistAll(anyList())).willReturn(List.of(1L));

            // when
            sut.update(pgId, updateData);

            // then
            then(productReadManager).should().findByProductGroupId(pgId);
            then(productCommandManager)
                    .should(org.mockito.Mockito.atLeastOnce())
                    .persistAll(anyList());
        }
    }
}
