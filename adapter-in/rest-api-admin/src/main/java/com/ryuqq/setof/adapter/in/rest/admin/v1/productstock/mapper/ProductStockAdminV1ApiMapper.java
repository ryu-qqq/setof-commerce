package com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.CreateOptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.CreateOptionV1ApiRequest.CreateOptionDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.UpdateProductStockV1ApiRequest;
import com.ryuqq.setof.application.product.dto.command.UpdateProductOptionCommand;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Admin V1 Product Stock Mapper
 *
 * <p>Application Response를 Admin V1 API Response로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.GodClass"})
@Component
public class ProductStockAdminV1ApiMapper {

    /**
     * V1 재고 수정 요청을 Application Command로 변환
     *
     * @param request V1 재고 수정 요청
     * @return SetStockCommand
     */
    public SetStockCommand toSetStockCommand(UpdateProductStockV1ApiRequest request) {
        return new SetStockCommand(
                request.productId(),
                request.productStockQuantity() != null ? request.productStockQuantity() : 0);
    }

    /**
     * V1 옵션 수정 요청을 Application Command로 변환
     *
     * @param request V1 옵션 수정 요청
     * @param sellerId 판매자 ID
     * @return UpdateProductOptionCommand
     */
    public UpdateProductOptionCommand toUpdateProductOptionCommand(
            CreateOptionV1ApiRequest request, Long sellerId) {
        List<CreateOptionDetailV1ApiRequest> options = request.options();

        String option1Name = null;
        String option1Value = null;
        String option2Name = null;
        String option2Value = null;

        if (options != null && !options.isEmpty()) {
            CreateOptionDetailV1ApiRequest firstOption = options.get(0);
            option1Name = firstOption.optionName();
            option1Value = firstOption.optionValue();

            if (options.size() > 1) {
                CreateOptionDetailV1ApiRequest secondOption = options.get(1);
                option2Name = secondOption.optionName();
                option2Value = secondOption.optionValue();
            }
        }

        return new UpdateProductOptionCommand(
                request.productId(),
                sellerId,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                request.additionalPrice(),
                request.quantity());
    }
}
