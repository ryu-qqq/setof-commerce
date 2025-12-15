package com.ryuqq.setof.application.product.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.product.dto.command.ProductSkuCommandDto;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.facade.ProductRegistrationFacade;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RegisterFullProductService")
@ExtendWith(MockitoExtension.class)
class RegisterFullProductServiceTest {

    @Mock private ProductRegistrationFacade registrationFacade;

    private RegisterFullProductService registerFullProductService;

    @BeforeEach
    void setUp() {
        registerFullProductService = new RegisterFullProductService(registrationFacade);
    }

    @Nested
    @DisplayName("registerFullProduct")
    class RegisterFullProductTest {

        @Test
        @DisplayName("전체 상품 등록 성공")
        void shouldRegisterFullProduct() {
            // Given
            RegisterFullProductCommand command = createValidCommand();
            Long expectedProductGroupId = 1L;

            when(registrationFacade.registerAll(any())).thenReturn(expectedProductGroupId);

            // When
            Long result = registerFullProductService.registerFullProduct(command);

            // Then
            assertNotNull(result);
            assertEquals(expectedProductGroupId, result);
            verify(registrationFacade, times(1)).registerAll(command);
        }

        private RegisterFullProductCommand createValidCommand() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    createProductSkus(),
                    List.of(),
                    null,
                    null);
        }

        private List<ProductSkuCommandDto> createProductSkus() {
            return List.of(new ProductSkuCommandDto("색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100));
        }
    }
}
