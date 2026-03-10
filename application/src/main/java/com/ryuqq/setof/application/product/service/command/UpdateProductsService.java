package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.internal.ProductCommandCoordinator;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductsUseCase;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateProductsService implements UpdateProductsUseCase {

    private final SellerOptionCommandCoordinator sellerOptionCoordinator;
    private final ProductCommandFactory productCommandFactory;
    private final ProductCommandCoordinator productCoordinator;

    public UpdateProductsService(
            SellerOptionCommandCoordinator sellerOptionCoordinator,
            ProductCommandFactory productCommandFactory,
            ProductCommandCoordinator productCoordinator) {
        this.sellerOptionCoordinator = sellerOptionCoordinator;
        this.productCommandFactory = productCommandFactory;
        this.productCoordinator = productCoordinator;
    }

    @Override
    @Transactional
    public void execute(UpdateProductsCommand command) {
        // 1. 옵션 수정 → resolvedActiveValueIds 획득
        UpdateSellerOptionGroupsCommand optionCmd = productCommandFactory.toOptionCommand(command);
        SellerOptionUpdateResult optionResult = sellerOptionCoordinator.update(optionCmd);

        // 2. Factory가 이름 → ID resolve + ProductUpdateData 생성
        ProductGroupId pgId = ProductGroupId.of(command.productGroupId());
        List<ProductDiffUpdateEntry> entries = productCommandFactory.toEntries(command.products());
        ProductUpdateData updateData =
                productCommandFactory.toUpdateData(
                        pgId,
                        entries,
                        optionCmd.optionGroups(),
                        optionResult.resolvedActiveValueIds(),
                        optionResult.occurredAt());

        // 3. Coordinator가 도메인 diff 기반 수정
        productCoordinator.update(pgId, updateData);
    }
}
