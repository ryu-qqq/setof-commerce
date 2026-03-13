package com.ryuqq.setof.application.product.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.product.ProductCommandFixtures;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
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
@DisplayName("UpdateProductPriceService 단위 테스트")
class UpdateProductPriceServiceTest {

    @InjectMocks private UpdateProductPriceService sut;

    @Mock private ProductCommandFactory productCommandFactory;
    @Mock private ProductReadManager productReadManager;
    @Mock private ProductCommandManager productCommandManager;

    @Nested
    @DisplayName("execute() - 상품 가격 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품 가격을 수정하고 저장한다")
        void execute_ValidCommand_UpdatesPriceAndPersists() {
            // given
            long productId = 1L;
            UpdateProductPriceCommand command =
                    ProductCommandFixtures.updatePriceCommand(productId);
            ProductId pid = ProductId.of(productId);
            StatusChangeContext<ProductId> context =
                    new StatusChangeContext<>(pid, Instant.parse("2024-01-01T00:00:00Z"));
            Product product = ProductFixtures.activeProduct(productId);

            given(productCommandFactory.createPriceUpdateContext(command)).willReturn(context);
            given(productReadManager.getById(pid)).willReturn(product);

            // when
            sut.execute(command);

            // then
            then(productCommandFactory).should().createPriceUpdateContext(command);
            then(productReadManager).should().getById(pid);
            then(productCommandManager).should().persist(product);
        }

        @Test
        @DisplayName("가격 수정 후 도메인 상태가 업데이트된다")
        void execute_AfterExecution_ProductPriceIsUpdated() {
            // given
            long productId = 1L;
            UpdateProductPriceCommand command =
                    new UpdateProductPriceCommand(productId, 60000, 55000);
            ProductId pid = ProductId.of(productId);
            StatusChangeContext<ProductId> context =
                    new StatusChangeContext<>(pid, Instant.parse("2024-01-01T00:00:00Z"));
            Product product = ProductFixtures.activeProduct(productId);

            given(productCommandFactory.createPriceUpdateContext(command)).willReturn(context);
            given(productReadManager.getById(pid)).willReturn(product);

            // when
            sut.execute(command);

            // then
            then(productCommandManager).should().persist(any(Product.class));
        }
    }
}
