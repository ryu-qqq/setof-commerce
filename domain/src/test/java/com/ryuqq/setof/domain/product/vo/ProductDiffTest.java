package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.setof.commerce.domain.product.ProductFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductDiff Value Object 테스트")
class ProductDiffTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("added/removed/retained/occurredAt으로 ProductDiff를 생성한다")
        void createWithAllFields() {
            // given
            List<Product> added = List.of(ProductFixtures.newProduct());
            List<Product> removed = List.of(ProductFixtures.deletedProduct());
            List<Product> retained = List.of(ProductFixtures.activeProduct());
            Instant occurredAt = CommonVoFixtures.now();

            // when
            ProductDiff diff = ProductDiff.of(added, removed, retained, occurredAt);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(occurredAt);
        }

        @Test
        @DisplayName("빈 리스트로 ProductDiff를 생성할 수 있다")
        void createWithEmptyLists() {
            // when
            ProductDiff diff =
                    ProductDiff.of(List.of(), List.of(), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
        }

        @Test
        @DisplayName("생성된 ProductDiff의 리스트는 불변이다")
        void listsAreImmutable() {
            // given
            ProductDiff diff = ProductFixtures.noChangeDiff();

            // when & then
            try {
                diff.added().add(ProductFixtures.newProduct());
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 여부 확인")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 removed가 모두 비어있으면 hasNoChanges()가 true이다")
        void hasNoChangesReturnsTrueWhenEmpty() {
            // given
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(),
                            List.of(),
                            List.of(ProductFixtures.activeProduct()),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenAdded() {
            // given
            ProductDiff diff = ProductFixtures.addOnlyDiff();

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenRemoved() {
            // given
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(),
                            List.of(ProductFixtures.deletedProduct()),
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("added와 removed가 모두 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenBothChanged() {
            // given
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(ProductFixtures.newProduct()),
                            List.of(ProductFixtures.deletedProduct()),
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }

    @Nested
    @DisplayName("allDirtyProducts() - 영속화 대상 상품 목록 반환")
    class AllDirtyProductsTest {

        @Test
        @DisplayName("retained + removed 합산 목록을 반환한다")
        void returnsCombinedRetainedAndRemoved() {
            // given
            Product retainedProduct = ProductFixtures.activeProduct(1L);
            Product removedProduct = ProductFixtures.deletedProduct();
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(),
                            List.of(removedProduct),
                            List.of(retainedProduct),
                            CommonVoFixtures.now());

            // when
            List<Product> dirtyProducts = diff.allDirtyProducts();

            // then
            assertThat(dirtyProducts).hasSize(2);
            assertThat(dirtyProducts).contains(retainedProduct, removedProduct);
        }

        @Test
        @DisplayName("retained과 removed 모두 비어있으면 빈 목록을 반환한다")
        void returnsEmptyListWhenBothEmpty() {
            // given
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(ProductFixtures.newProduct()),
                            List.of(),
                            List.of(),
                            CommonVoFixtures.now());

            // when
            List<Product> dirtyProducts = diff.allDirtyProducts();

            // then
            assertThat(dirtyProducts).isEmpty();
        }

        @Test
        @DisplayName("allDirtyProducts()는 added 상품을 포함하지 않는다")
        void doesNotIncludeAddedProducts() {
            // given
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(ProductFixtures.newProduct()),
                            List.of(),
                            List.of(ProductFixtures.activeProduct()),
                            CommonVoFixtures.now());

            // when
            List<Product> dirtyProducts = diff.allDirtyProducts();

            // then
            assertThat(dirtyProducts).hasSize(1);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("ProductDiff는 record 타입으로 added/removed/retained/occurredAt 필드를 노출한다")
        void exposesAllFields() {
            // given
            Instant now = CommonVoFixtures.now();
            ProductDiff diff =
                    ProductDiff.of(
                            List.of(), List.of(), List.of(ProductFixtures.activeProduct()), now);

            // then
            assertThat(diff.added()).isNotNull();
            assertThat(diff.removed()).isNotNull();
            assertThat(diff.retained()).isNotNull();
            assertThat(diff.occurredAt()).isEqualTo(now);
        }
    }
}
