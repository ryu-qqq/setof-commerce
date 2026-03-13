package com.ryuqq.setof.application.product.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.product.ProductCommandFixtures;
import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.internal.ProductCommandCoordinator;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.time.Instant;
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
@DisplayName("UpdateProductsService 단위 테스트")
class UpdateProductsServiceTest {

    @InjectMocks private UpdateProductsService sut;

    @Mock private SellerOptionCommandCoordinator sellerOptionCoordinator;
    @Mock private ProductCommandFactory productCommandFactory;
    @Mock private ProductCommandCoordinator productCoordinator;
    @Mock private ProductGroupReadManager productGroupReadManager;

    @Nested
    @DisplayName("execute() - 상품 목록 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 옵션 수정 후 상품을 수정한다")
        void execute_ValidCommand_UpdatesOptionsAndProducts() {
            // given
            long productGroupId = 1L;
            UpdateProductsCommand command =
                    ProductCommandFixtures.updateProductsCommand(productGroupId);

            UpdateSellerOptionGroupsCommand optionCmd =
                    new UpdateSellerOptionGroupsCommand(productGroupId, List.of());
            List<SellerOptionValueId> resolvedValueIds = List.of(SellerOptionValueId.of(100L));
            Instant occurredAt = Instant.parse("2024-01-01T00:00:00Z");
            SellerOptionUpdateResult optionResult =
                    new SellerOptionUpdateResult(resolvedValueIds, occurredAt, false);

            List<ProductDiffUpdateEntry> entries = ProductCommandFixtures.defaultEntries(1L);
            ProductGroupId pgId = ProductGroupId.of(productGroupId);
            ProductUpdateData updateData = new ProductUpdateData(pgId, List.of(), occurredAt);

            given(productCommandFactory.toOptionCommand(command)).willReturn(optionCmd);
            given(sellerOptionCoordinator.update(optionCmd)).willReturn(optionResult);
            given(productCommandFactory.toEntries(command.products())).willReturn(entries);
            given(
                            productCommandFactory.toUpdateData(
                                    eq(pgId),
                                    eq(entries),
                                    any(),
                                    eq(resolvedValueIds),
                                    eq(occurredAt)))
                    .willReturn(updateData);

            // when
            sut.execute(command);

            // then
            then(productCommandFactory).should().toOptionCommand(command);
            then(sellerOptionCoordinator).should().update(optionCmd);
            then(productCommandFactory).should().toEntries(command.products());
            then(productCoordinator).should().update(pgId, updateData);
        }
    }
}
