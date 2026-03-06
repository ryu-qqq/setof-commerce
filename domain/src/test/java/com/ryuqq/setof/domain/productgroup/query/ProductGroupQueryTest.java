package com.ryuqq.setof.domain.productgroup.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroup Query 객체 테스트")
class ProductGroupQueryTest {

    @Nested
    @DisplayName("ProductGroupSortKey 테스트")
    class ProductGroupSortKeyTest {

        @Test
        @DisplayName("CREATED_AT의 fieldName은 'createdAt'이다")
        void createdAtFieldName() {
            assertThat(ProductGroupSortKey.CREATED_AT.fieldName()).isEqualTo("createdAt");
        }

        @Test
        @DisplayName("UPDATED_AT의 fieldName은 'updatedAt'이다")
        void updatedAtFieldName() {
            assertThat(ProductGroupSortKey.UPDATED_AT.fieldName()).isEqualTo("updatedAt");
        }

        @Test
        @DisplayName("NAME의 fieldName은 'productGroupName'이다")
        void nameFieldName() {
            assertThat(ProductGroupSortKey.NAME.fieldName()).isEqualTo("productGroupName");
        }

        @Test
        @DisplayName("CURRENT_PRICE의 fieldName은 'currentPrice'이다")
        void currentPriceFieldName() {
            assertThat(ProductGroupSortKey.CURRENT_PRICE.fieldName()).isEqualTo("currentPrice");
        }

        @Test
        @DisplayName("SALE_PRICE의 fieldName은 'salePrice'이다")
        void salePriceFieldName() {
            assertThat(ProductGroupSortKey.SALE_PRICE.fieldName()).isEqualTo("salePrice");
        }

        @Test
        @DisplayName("defaultKey()는 CREATED_AT을 반환한다")
        void defaultKeyIsCreatedAt() {
            assertThat(ProductGroupSortKey.defaultKey()).isEqualTo(ProductGroupSortKey.CREATED_AT);
        }

        @Test
        @DisplayName("모든 정렬 키가 존재한다")
        void allValuesExist() {
            assertThat(ProductGroupSortKey.values())
                    .containsExactly(
                            ProductGroupSortKey.CREATED_AT,
                            ProductGroupSortKey.UPDATED_AT,
                            ProductGroupSortKey.NAME,
                            ProductGroupSortKey.CURRENT_PRICE,
                            ProductGroupSortKey.SALE_PRICE);
        }
    }

    @Nested
    @DisplayName("ProductGroupSearchCriteria 테스트")
    class ProductGroupSearchCriteriaTest {

        private CursorQueryContext<ProductGroupSortKey, Long> defaultQueryContext() {
            return CursorQueryContext.defaultOf(ProductGroupSortKey.defaultKey());
        }

        @Nested
        @DisplayName("생성 테스트")
        class CreationTest {

            @Test
            @DisplayName("queryContext만으로 기본 검색 조건을 생성한다")
            void createWithDefaultCriteria() {
                // given
                var queryContext = defaultQueryContext();

                // when
                var criteria = ProductGroupSearchCriteria.of(queryContext);

                // then
                assertThat(criteria.queryContext()).isNotNull();
                assertThat(criteria.sellerId()).isNull();
                assertThat(criteria.brandId()).isNull();
                assertThat(criteria.categoryId()).isNull();
                assertThat(criteria.status()).isNull();
                assertThat(criteria.searchWord()).isNull();
            }

            @Test
            @DisplayName("queryContext가 null이면 예외가 발생한다")
            void throwExceptionForNullQueryContext() {
                assertThatThrownBy(() -> ProductGroupSearchCriteria.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("queryContext");
            }

            @Test
            @DisplayName("모든 필터를 포함하여 생성한다")
            void createWithAllFilters() {
                // given
                var queryContext = defaultQueryContext();
                SellerId sellerId = SellerId.of(1L);
                BrandId brandId = BrandId.of(1L);
                CategoryId categoryId = CategoryId.of(1L);

                // when
                var criteria =
                        new ProductGroupSearchCriteria(
                                sellerId,
                                brandId,
                                categoryId,
                                List.of(),
                                List.of(),
                                ProductGroupStatus.ACTIVE,
                                ProductGroupSearchField.PRODUCT_GROUP_NAME,
                                "검색어",
                                null,
                                null,
                                null,
                                queryContext);

                // then
                assertThat(criteria.sellerId()).isEqualTo(sellerId);
                assertThat(criteria.brandId()).isEqualTo(brandId);
                assertThat(criteria.categoryId()).isEqualTo(categoryId);
                assertThat(criteria.status()).isEqualTo(ProductGroupStatus.ACTIVE);
                assertThat(criteria.searchWord()).isEqualTo("검색어");
            }
        }

        @Nested
        @DisplayName("hasXxx() 메서드 테스트")
        class HasMethodTest {

            @Test
            @DisplayName("hasSellerId() - sellerId가 있으면 true를 반환한다")
            void hasSellerIdReturnsTrue() {
                var criteria =
                        new ProductGroupSearchCriteria(
                                SellerId.of(1L),
                                null,
                                null,
                                List.of(),
                                List.of(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                defaultQueryContext());
                assertThat(criteria.hasSellerId()).isTrue();
            }

            @Test
            @DisplayName("hasSellerId() - sellerId가 없으면 false를 반환한다")
            void hasSellerIdReturnsFalse() {
                var criteria = ProductGroupSearchCriteria.of(defaultQueryContext());
                assertThat(criteria.hasSellerId()).isFalse();
            }

            @Test
            @DisplayName("hasBrandId() - brandId가 있으면 true를 반환한다")
            void hasBrandIdReturnsTrue() {
                var criteria =
                        new ProductGroupSearchCriteria(
                                null,
                                BrandId.of(1L),
                                null,
                                List.of(),
                                List.of(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                defaultQueryContext());
                assertThat(criteria.hasBrandId()).isTrue();
            }

            @Test
            @DisplayName("hasBrandId() - brandId가 없으면 false를 반환한다")
            void hasBrandIdReturnsFalse() {
                var criteria = ProductGroupSearchCriteria.of(defaultQueryContext());
                assertThat(criteria.hasBrandId()).isFalse();
            }

            @Test
            @DisplayName("hasCategoryId() - categoryId가 있으면 true를 반환한다")
            void hasCategoryIdReturnsTrue() {
                var criteria =
                        new ProductGroupSearchCriteria(
                                null,
                                null,
                                CategoryId.of(1L),
                                List.of(),
                                List.of(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                defaultQueryContext());
                assertThat(criteria.hasCategoryId()).isTrue();
            }

            @Test
            @DisplayName("hasCategoryId() - categoryId가 없으면 false를 반환한다")
            void hasCategoryIdReturnsFalse() {
                var criteria = ProductGroupSearchCriteria.of(defaultQueryContext());
                assertThat(criteria.hasCategoryId()).isFalse();
            }

            @Test
            @DisplayName("hasStatus() - status가 있으면 true를 반환한다")
            void hasStatusReturnsTrue() {
                var criteria =
                        new ProductGroupSearchCriteria(
                                null,
                                null,
                                null,
                                List.of(),
                                List.of(),
                                ProductGroupStatus.ACTIVE,
                                null,
                                null,
                                null,
                                null,
                                null,
                                defaultQueryContext());
                assertThat(criteria.hasStatus()).isTrue();
            }

            @Test
            @DisplayName("hasStatus() - status가 없으면 false를 반환한다")
            void hasStatusReturnsFalse() {
                var criteria = ProductGroupSearchCriteria.of(defaultQueryContext());
                assertThat(criteria.hasStatus()).isFalse();
            }

            @Test
            @DisplayName("hasSearchCondition() - searchWord가 있으면 true를 반환한다")
            void hasSearchConditionReturnsTrue() {
                var criteria =
                        new ProductGroupSearchCriteria(
                                null,
                                null,
                                null,
                                List.of(),
                                List.of(),
                                null,
                                null,
                                "검색어",
                                null,
                                null,
                                null,
                                defaultQueryContext());
                assertThat(criteria.hasSearchCondition()).isTrue();
            }

            @Test
            @DisplayName("hasSearchCondition() - searchWord가 null이면 false를 반환한다")
            void hasSearchConditionReturnsFalseForNull() {
                var criteria = ProductGroupSearchCriteria.of(defaultQueryContext());
                assertThat(criteria.hasSearchCondition()).isFalse();
            }

            @Test
            @DisplayName("hasSearchCondition() - searchWord가 빈 값이면 false를 반환한다")
            void hasSearchConditionReturnsFalseForBlank() {
                var criteria =
                        new ProductGroupSearchCriteria(
                                null,
                                null,
                                null,
                                List.of(),
                                List.of(),
                                null,
                                null,
                                "  ",
                                null,
                                null,
                                null,
                                defaultQueryContext());
                assertThat(criteria.hasSearchCondition()).isFalse();
            }
        }
    }
}
