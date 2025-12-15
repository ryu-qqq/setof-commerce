package com.ryuqq.setof.application.product.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.product.facade.ProductModificationFacade;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.domain.product.ProductGroupFixture;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpdateFullProductService")
@ExtendWith(MockitoExtension.class)
class UpdateFullProductServiceTest {

    @Mock private ProductModificationFacade modificationFacade;
    @Mock private ProductGroupReadManager productGroupReadManager;

    private UpdateFullProductService updateFullProductService;

    @BeforeEach
    void setUp() {
        updateFullProductService =
                new UpdateFullProductService(modificationFacade, productGroupReadManager);
    }

    @Nested
    @DisplayName("updateFullProduct")
    class UpdateFullProductTest {

        @Test
        @DisplayName("전체 상품 수정 성공")
        void shouldUpdateFullProduct() {
            // Given
            UpdateFullProductCommand command = createValidCommand();
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);

            when(productGroupReadManager.findById(1L)).thenReturn(existingProductGroup);
            doNothing().when(modificationFacade).updateAll(any());

            // When
            updateFullProductService.updateFullProduct(command);

            // Then
            verify(productGroupReadManager, times(1)).findById(1L);
            verify(modificationFacade, times(1)).updateAll(command);
        }

        private UpdateFullProductCommand createValidCommand() {
            return new UpdateFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "수정된 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(55000),
                    BigDecimal.valueOf(50000),
                    "ACTIVE",
                    1L,
                    1L,
                    List.of(),
                    List.of(),
                    null,
                    null);
        }
    }
}
