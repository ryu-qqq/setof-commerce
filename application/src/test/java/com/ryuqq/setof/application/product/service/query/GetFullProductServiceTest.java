package com.ryuqq.setof.application.product.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.facade.ProductQueryFacade;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetFullProductService")
@ExtendWith(MockitoExtension.class)
class GetFullProductServiceTest {

    @Mock private ProductQueryFacade queryFacade;

    private GetFullProductService getFullProductService;

    @BeforeEach
    void setUp() {
        getFullProductService = new GetFullProductService(queryFacade);
    }

    @Nested
    @DisplayName("getFullProduct")
    class GetFullProductTest {

        @Test
        @DisplayName("전체 상품 조회 성공")
        void shouldGetFullProduct() {
            // Given
            Long productGroupId = 1L;
            FullProductResponse expectedResponse = createFullProductResponse();

            when(queryFacade.getFullProduct(productGroupId)).thenReturn(expectedResponse);

            // When
            FullProductResponse result = getFullProductService.getFullProduct(productGroupId);

            // Then
            assertNotNull(result);
            assertEquals(expectedResponse, result);
            verify(queryFacade, times(1)).getFullProduct(productGroupId);
        }

        @Test
        @DisplayName("productGroupId가 null일 때 예외 발생")
        void shouldThrowExceptionWhenProductGroupIdIsNull() {
            // Given
            Long productGroupId = null;

            // When & Then
            IllegalArgumentException exception =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> getFullProductService.getFullProduct(productGroupId));
            assertEquals("상품그룹 ID는 필수입니다.", exception.getMessage());
            verify(queryFacade, never()).getFullProduct(anyLong());
        }

        @Test
        @DisplayName("Facade에서 반환된 Response가 정확히 전달됨")
        void shouldPassThroughFacadeResponse() {
            // Given
            Long productGroupId = 2L;
            FullProductResponse expectedResponse = createCustomFullProductResponse(2L);

            when(queryFacade.getFullProduct(productGroupId)).thenReturn(expectedResponse);

            // When
            FullProductResponse result = getFullProductService.getFullProduct(productGroupId);

            // Then
            assertEquals(2L, result.productGroup().productGroupId());
            assertEquals("커스텀 상품그룹", result.productGroup().name());
        }

        private FullProductResponse createFullProductResponse() {
            ProductGroupResponse productGroupResponse =
                    new ProductGroupResponse(
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

            return new FullProductResponse(
                    productGroupResponse, List.of(), List.of(), null, null, List.of());
        }

        private FullProductResponse createCustomFullProductResponse(Long id) {
            ProductGroupResponse productGroupResponse =
                    new ProductGroupResponse(
                            id,
                            1L,
                            100L,
                            1L,
                            "커스텀 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(60000),
                            BigDecimal.valueOf(55000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of());

            return new FullProductResponse(
                    productGroupResponse, List.of(), List.of(), null, null, List.of());
        }
    }
}
