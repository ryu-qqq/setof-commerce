package com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.CreateOptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.UpdateProductStockV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.mapper.ProductStockAdminV1ApiMapper;
import com.ryuqq.setof.application.product.dto.command.UpdateProductOptionCommand;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductOptionUseCase;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Product Stock Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Product Stock 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Product Stock (Legacy V1)", description = "레거시 Product Stock API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductStockAdminV1Controller {

    private final SetStockUseCase setStockUseCase;
    private final UpdateProductOptionUseCase updateProductOptionUseCase;
    private final ProductStockAdminV1ApiMapper productStockAdminV1ApiMapper;

    public ProductStockAdminV1Controller(
            SetStockUseCase setStockUseCase,
            UpdateProductOptionUseCase updateProductOptionUseCase,
            ProductStockAdminV1ApiMapper productStockAdminV1ApiMapper) {
        this.setStockUseCase = setStockUseCase;
        this.updateProductOptionUseCase = updateProductOptionUseCase;
        this.productStockAdminV1ApiMapper = productStockAdminV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 옵션 수정", description = "상품 그룹의 옵션을 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/option")
    public ResponseEntity<V1ApiResponse<Set<ProductFetchV1ApiResponse>>> updateProductOption(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @Valid @RequestBody List<CreateOptionV1ApiRequest> options) {

        Set<ProductFetchV1ApiResponse> responses =
                options.stream()
                        .map(
                                option -> {
                                    UpdateProductOptionCommand command =
                                            productStockAdminV1ApiMapper
                                                    .toUpdateProductOptionCommand(option, sellerId);
                                    updateProductOptionUseCase.execute(command);

                                    return new ProductFetchV1ApiResponse(
                                            option.productId(),
                                            option.quantity() != null ? option.quantity() : 0,
                                            "ACTIVE",
                                            buildOptionString(option),
                                            Collections.emptySet(),
                                            option.additionalPrice());
                                })
                        .collect(Collectors.toSet());

        return ResponseEntity.ok(V1ApiResponse.success(responses));
    }

    private String buildOptionString(CreateOptionV1ApiRequest option) {
        if (option.options() == null || option.options().isEmpty()) {
            return null;
        }
        return option.options().stream()
                .map(o -> o.optionName() != null && o.optionValue() != null ? o.optionValue() : "")
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 개별 상품 재고 수정", description = "개별 상품의 재고를 수정합니다.")
    @PatchMapping("/product/{productId}/stock")
    public ResponseEntity<V1ApiResponse<Set<ProductFetchV1ApiResponse>>> updateProductStock(
            @PathVariable long productId,
            @Valid @RequestBody UpdateProductStockV1ApiRequest updateProductStock) {

        SetStockCommand command =
                productStockAdminV1ApiMapper.toSetStockCommand(updateProductStock);
        setStockUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(Collections.emptySet()));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 재고 수정", description = "상품 그룹의 여러 상품 재고를 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/stock")
    public ResponseEntity<V1ApiResponse<Set<ProductFetchV1ApiResponse>>> updateProductStocks(
            @PathVariable long productGroupId,
            @Valid @RequestBody List<UpdateProductStockV1ApiRequest> updateProductStocks) {

        updateProductStocks.forEach(
                request -> {
                    SetStockCommand command =
                            productStockAdminV1ApiMapper.toSetStockCommand(request);
                    setStockUseCase.execute(command);
                });
        return ResponseEntity.ok(V1ApiResponse.success(Collections.emptySet()));
    }
}
