package com.ryuqq.setof.application.product.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FullProductAssembler")
class FullProductAssemblerTest {

    private FullProductAssembler fullProductAssembler;

    @BeforeEach
    void setUp() {
        fullProductAssembler = new FullProductAssembler();
    }

    @Nested
    @DisplayName("toFullProductResponse")
    class ToFullProductResponseTest {

        @Test
        @DisplayName("전체 상품 Response 조립 성공")
        void shouldAssembleFullProductResponse() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();
            List<ProductResponse> products = createProductResponses();
            List<ProductImageResponse> images = createImageResponses();
            ProductDescriptionResponse description = createDescriptionResponse();
            ProductNoticeResponse notice = createNoticeResponse();
            List<ProductStockResponse> stocks = createStockResponses();

            // When
            FullProductResponse result =
                    fullProductAssembler.toFullProductResponse(
                            productGroup, products, images, description, notice, stocks);

            // Then
            assertNotNull(result);
            assertEquals(productGroup, result.productGroup());
            assertEquals(products, result.products());
            assertEquals(images, result.images());
            assertEquals(description, result.description());
            assertEquals(notice, result.notice());
            assertEquals(stocks, result.stocks());
        }

        @Test
        @DisplayName("null products 전달 시 빈 리스트 반환")
        void shouldReturnEmptyListWhenProductsIsNull() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();

            // When
            FullProductResponse result =
                    fullProductAssembler.toFullProductResponse(
                            productGroup, null, List.of(), null, null, List.of());

            // Then
            assertNotNull(result);
            assertTrue(result.products().isEmpty());
        }

        @Test
        @DisplayName("null images 전달 시 빈 리스트 반환")
        void shouldReturnEmptyListWhenImagesIsNull() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();

            // When
            FullProductResponse result =
                    fullProductAssembler.toFullProductResponse(
                            productGroup, List.of(), null, null, null, List.of());

            // Then
            assertNotNull(result);
            assertTrue(result.images().isEmpty());
        }

        @Test
        @DisplayName("null stocks 전달 시 빈 리스트 반환")
        void shouldReturnEmptyListWhenStocksIsNull() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();

            // When
            FullProductResponse result =
                    fullProductAssembler.toFullProductResponse(
                            productGroup, List.of(), List.of(), null, null, null);

            // Then
            assertNotNull(result);
            assertTrue(result.stocks().isEmpty());
        }

        @Test
        @DisplayName("description null 허용")
        void shouldAllowNullDescription() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();

            // When
            FullProductResponse result =
                    fullProductAssembler.toFullProductResponse(
                            productGroup, List.of(), List.of(), null, null, List.of());

            // Then
            assertNotNull(result);
            assertNull(result.description());
        }

        @Test
        @DisplayName("notice null 허용")
        void shouldAllowNullNotice() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();

            // When
            FullProductResponse result =
                    fullProductAssembler.toFullProductResponse(
                            productGroup, List.of(), List.of(), null, null, List.of());

            // Then
            assertNotNull(result);
            assertNull(result.notice());
        }
    }

    @Nested
    @DisplayName("toBasicFullProductResponse")
    class ToBasicFullProductResponseTest {

        @Test
        @DisplayName("기본 Response 조립 성공")
        void shouldAssembleBasicResponse() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();
            List<ProductResponse> products = createProductResponses();

            // When
            FullProductResponse result =
                    fullProductAssembler.toBasicFullProductResponse(productGroup, products);

            // Then
            assertNotNull(result);
            assertEquals(productGroup, result.productGroup());
            assertEquals(products, result.products());
            assertTrue(result.images().isEmpty());
            assertNull(result.description());
            assertNull(result.notice());
            assertTrue(result.stocks().isEmpty());
        }

        @Test
        @DisplayName("null products 전달 시 빈 리스트 반환")
        void shouldReturnEmptyListWhenProductsIsNullInBasic() {
            // Given
            ProductGroupResponse productGroup = createProductGroupResponse();

            // When
            FullProductResponse result =
                    fullProductAssembler.toBasicFullProductResponse(productGroup, null);

            // Then
            assertNotNull(result);
            assertTrue(result.products().isEmpty());
        }
    }

    // ========== Helper Methods ==========

    private ProductGroupResponse createProductGroupResponse() {
        return new ProductGroupResponse(
                1L,
                1L,
                100L,
                1L,
                "테스트 상품그룹",
                "TWO_LEVEL",
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(45000),
                "ACTIVE",
                1L,
                1L,
                List.of());
    }

    private List<ProductResponse> createProductResponses() {
        return List.of(
                new ProductResponse(
                        1L,
                        1L,
                        "TWO_LEVEL",
                        "색상",
                        "Red",
                        "사이즈",
                        "M",
                        BigDecimal.ZERO,
                        false,
                        true));
    }

    private List<ProductImageResponse> createImageResponses() {
        return List.of(
                new ProductImageResponse(
                        1L,
                        1L,
                        "MAIN",
                        "https://origin.example.com/image1.jpg",
                        "https://cdn.example.com/image1.jpg",
                        1,
                        Instant.now()));
    }

    private ProductDescriptionResponse createDescriptionResponse() {
        return new ProductDescriptionResponse(
                1L, 1L, "<p>상세 설명</p>", List.of(), true, true, Instant.now(), Instant.now());
    }

    private ProductNoticeResponse createNoticeResponse() {
        return new ProductNoticeResponse(1L, 1L, 1L, List.of(), 0, Instant.now(), Instant.now());
    }

    private List<ProductStockResponse> createStockResponses() {
        return List.of(new ProductStockResponse(1L, 1L, 100, Instant.now()));
    }
}
