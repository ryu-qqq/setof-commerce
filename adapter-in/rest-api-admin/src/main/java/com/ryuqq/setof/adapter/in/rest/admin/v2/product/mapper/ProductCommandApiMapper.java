package com.ryuqq.setof.adapter.in.rest.admin.v2.product.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductPriceApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductStockApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductsApiRequest;
import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import org.springframework.stereotype.Component;

/**
 * ProductCommandApiMapper - 상품(SKU) Command API 변환 매퍼.
 *
 * <p>API Request와 Application Command 간 변환을 담당합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>CQRS 분리: Command 전용 Mapper (QueryApiMapper와 분리).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductCommandApiMapper {

    /**
     * UpdateProductPriceApiRequest -> UpdateProductPriceCommand 변환.
     *
     * @param productId 상품 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductPriceCommand toCommand(
            Long productId, UpdateProductPriceApiRequest request) {
        return new UpdateProductPriceCommand(
                productId, request.regularPrice(), request.currentPrice());
    }

    /**
     * UpdateProductStockApiRequest -> UpdateProductStockCommand 변환.
     *
     * @param productId 상품 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductStockCommand toCommand(
            Long productId, UpdateProductStockApiRequest request) {
        return new UpdateProductStockCommand(productId, request.stockQuantity());
    }

    /**
     * UpdateProductsApiRequest -> UpdateProductsCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductsCommand toCommand(Long productGroupId, UpdateProductsApiRequest request) {
        return new UpdateProductsCommand(
                productGroupId,
                request.optionGroups().stream()
                        .map(
                                g ->
                                        new UpdateProductsCommand.OptionGroupData(
                                                g.sellerOptionGroupId(),
                                                g.optionGroupName(),
                                                g.sortOrder(),
                                                g.optionValues().stream()
                                                        .map(
                                                                v ->
                                                                        new UpdateProductsCommand
                                                                                .OptionValueData(
                                                                                v
                                                                                        .sellerOptionValueId(),
                                                                                v.optionValueName(),
                                                                                v.sortOrder()))
                                                        .toList()))
                        .toList(),
                request.products().stream()
                        .map(
                                p ->
                                        new UpdateProductsCommand.ProductData(
                                                p.productId(),
                                                p.skuCode(),
                                                p.regularPrice(),
                                                p.currentPrice(),
                                                p.stockQuantity(),
                                                p.sortOrder(),
                                                p.selectedOptions().stream()
                                                        .map(
                                                                so ->
                                                                        new SelectedOption(
                                                                                so
                                                                                        .optionGroupName(),
                                                                                so
                                                                                        .optionValueName()))
                                                        .toList()))
                        .toList());
    }
}
