package com.ryuqq.setof.application.product.dto.bundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.product.ProductFixture;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productdescription.ProductDescriptionFixture;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.ProductImageFixture;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductSubAggregatesPersistBundle")
class ProductSubAggregatesPersistBundleTest {

    @Nested
    @DisplayName("생성")
    class CreateTest {

        @Test
        @DisplayName("유효한 Bundle 생성 성공")
        void shouldCreateValidBundle() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            List<ProductImage> images = ProductImageFixture.createImageSet(2);
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // When
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, images, description, notice);

            // Then
            assertNotNull(bundle);
            assertEquals(1, bundle.products().size());
            assertEquals(3, bundle.images().size());
            assertNotNull(bundle.description());
            assertNotNull(bundle.notice());
        }

        @Test
        @DisplayName("빈 이미지 리스트로 Bundle 생성 성공")
        void shouldCreateBundleWithEmptyImages() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // When
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, List.of(), description, notice);

            // Then
            assertNotNull(bundle);
            assertTrue(bundle.images().isEmpty());
            assertFalse(bundle.hasImages());
        }

        @Test
        @DisplayName("null 이미지 리스트는 빈 리스트로 변환")
        void shouldConvertNullImagesToEmptyList() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // When
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, null, description, notice);

            // Then
            assertNotNull(bundle.images());
            assertTrue(bundle.images().isEmpty());
        }

        @Test
        @DisplayName("products가 null이면 예외 발생")
        void shouldThrowExceptionWhenProductsIsNull() {
            // Given
            List<ProductImage> images = ProductImageFixture.createImageSet(1);
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // When & Then
            assertThrows(
                    NullPointerException.class,
                    () -> new ProductSubAggregatesPersistBundle(null, images, description, notice));
        }

        @Test
        @DisplayName("products가 비어있으면 예외 발생")
        void shouldThrowExceptionWhenProductsIsEmpty() {
            // Given
            List<ProductImage> images = ProductImageFixture.createImageSet(1);
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            new ProductSubAggregatesPersistBundle(
                                    List.of(), images, description, notice));
        }

        @Test
        @DisplayName("description이 null이면 예외 발생")
        void shouldThrowExceptionWhenDescriptionIsNull() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            List<ProductImage> images = ProductImageFixture.createImageSet(1);
            ProductNotice notice = ProductNoticeFixture.createNotice();

            // When & Then
            assertThrows(
                    NullPointerException.class,
                    () -> new ProductSubAggregatesPersistBundle(products, images, null, notice));
        }

        @Test
        @DisplayName("notice가 null이면 예외 발생")
        void shouldThrowExceptionWhenNoticeIsNull() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            List<ProductImage> images = ProductImageFixture.createImageSet(1);
            ProductDescription description = ProductDescriptionFixture.create();

            // When & Then
            assertThrows(
                    NullPointerException.class,
                    () ->
                            new ProductSubAggregatesPersistBundle(
                                    products, images, description, null));
        }
    }

    @Nested
    @DisplayName("hasImages")
    class HasImagesTest {

        @Test
        @DisplayName("이미지가 있으면 true 반환")
        void shouldReturnTrueWhenImagesExist() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            List<ProductImage> images = ProductImageFixture.createImageSet(2);
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, images, description, notice);

            // When & Then
            assertTrue(bundle.hasImages());
        }

        @Test
        @DisplayName("이미지가 없으면 false 반환")
        void shouldReturnFalseWhenNoImages() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, List.of(), description, notice);

            // When & Then
            assertFalse(bundle.hasImages());
        }
    }

    @Nested
    @DisplayName("productCount")
    class ProductCountTest {

        @Test
        @DisplayName("상품 개수 반환")
        void shouldReturnProductCount() {
            // Given
            List<Product> products = ProductFixture.createList();
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, List.of(), description, notice);

            // When & Then
            assertEquals(6, bundle.productCount());
        }
    }
}
