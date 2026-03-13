package com.ryuqq.setof.application.product.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.product.ProductCommandFixtures;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.manager.ProductCommandManager;
import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.setof.commerce.domain.product.ProductFixtures;
import java.time.Instant;
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
@DisplayName("UpdateProductStockService 단위 테스트")
class UpdateProductStockServiceTest {

    @InjectMocks private UpdateProductStockService sut;

    @Mock private ProductCommandFactory productCommandFactory;
    @Mock private ProductReadManager productReadManager;
    @Mock private ProductCommandManager productCommandManager;

    @Nested
    @DisplayName("execute() - 상품 재고 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품 재고를 수정하고 저장한다")
        void execute_ValidCommand_UpdatesStockAndPersists() {
            // given
            long productId = 1L;
            UpdateProductStockCommand command =
                    ProductCommandFixtures.updateStockCommand(productId);
            ProductId pid = ProductId.of(productId);
            StatusChangeContext<ProductId> context =
                    new StatusChangeContext<>(pid, Instant.parse("2024-01-01T00:00:00Z"));
            Product product = ProductFixtures.activeProduct(productId);

            given(productCommandFactory.createStockUpdateContext(command)).willReturn(context);
            given(productReadManager.getById(pid)).willReturn(product);

            // when
            sut.execute(command);

            // then
            then(productCommandFactory).should().createStockUpdateContext(command);
            then(productReadManager).should().getById(pid);
            then(productCommandManager).should().persist(product);
        }

        @Test
        @DisplayName("재고 0으로 수정해도 정상적으로 처리된다")
        void execute_ZeroStock_UpdatesSuccessfully() {
            // given
            long productId = 1L;
            UpdateProductStockCommand command = new UpdateProductStockCommand(productId, 0);
            ProductId pid = ProductId.of(productId);
            StatusChangeContext<ProductId> context =
                    new StatusChangeContext<>(pid, Instant.parse("2024-01-01T00:00:00Z"));
            Product product = ProductFixtures.activeProduct(productId);

            given(productCommandFactory.createStockUpdateContext(command)).willReturn(context);
            given(productReadManager.getById(pid)).willReturn(product);

            // when
            sut.execute(command);

            // then
            then(productCommandManager).should().persist(any(Product.class));
        }
    }
}
