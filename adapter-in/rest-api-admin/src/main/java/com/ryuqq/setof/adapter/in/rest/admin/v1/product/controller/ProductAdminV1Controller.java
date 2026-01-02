package com.ryuqq.setof.adapter.in.rest.admin.v1.product.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreatePriceV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.DeleteProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateCategoryV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdatePriceContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.query.ProductGroupFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.CreateProductGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.mapper.ProductAdminV1ApiMapper;
import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.MarkProductOutOfStockCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductDisplayCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.command.DeleteProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.MarkProductOutOfStockUseCase;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductDisplayUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductPriceUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Product Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Product 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Product (Legacy V1)", description = "레거시 Product API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductAdminV1Controller {

    private final GetFullProductUseCase getFullProductUseCase;
    private final GetProductGroupsUseCase getProductGroupsUseCase;
    private final RegisterFullProductUseCase registerFullProductUseCase;
    private final DeleteProductGroupUseCase deleteProductGroupUseCase;
    private final UpdateProductGroupUseCase updateProductGroupUseCase;
    private final UpdateProductPriceUseCase updateProductPriceUseCase;
    private final UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;
    private final UpdateProductDisplayUseCase updateProductDisplayUseCase;
    private final MarkProductOutOfStockUseCase markProductOutOfStockUseCase;
    private final ProductAdminV1ApiMapper mapper;

    public ProductAdminV1Controller(
            GetFullProductUseCase getFullProductUseCase,
            GetProductGroupsUseCase getProductGroupsUseCase,
            RegisterFullProductUseCase registerFullProductUseCase,
            DeleteProductGroupUseCase deleteProductGroupUseCase,
            UpdateProductGroupUseCase updateProductGroupUseCase,
            UpdateProductPriceUseCase updateProductPriceUseCase,
            UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase,
            UpdateProductDisplayUseCase updateProductDisplayUseCase,
            MarkProductOutOfStockUseCase markProductOutOfStockUseCase,
            ProductAdminV1ApiMapper mapper) {
        this.getFullProductUseCase = getFullProductUseCase;
        this.getProductGroupsUseCase = getProductGroupsUseCase;
        this.registerFullProductUseCase = registerFullProductUseCase;
        this.deleteProductGroupUseCase = deleteProductGroupUseCase;
        this.updateProductGroupUseCase = updateProductGroupUseCase;
        this.updateProductPriceUseCase = updateProductPriceUseCase;
        this.updateProductGroupStatusUseCase = updateProductGroupStatusUseCase;
        this.updateProductDisplayUseCase = updateProductDisplayUseCase;
        this.markProductOutOfStockUseCase = markProductOutOfStockUseCase;
        this.mapper = mapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 조회", description = "특정 상품 그룹을 조회합니다.")
    @GetMapping("/product/group/{productGroupId}")
    public ResponseEntity<V1ApiResponse<ProductGroupFetchV1ApiResponse>> fetchProductGroup(
            @PathVariable long productGroupId) {
        FullProductResponse response = getFullProductUseCase.getFullProduct(productGroupId);
        ProductGroupFetchV1ApiResponse v1Response = mapper.toFetchResponse(response);
        return ResponseEntity.ok(V1ApiResponse.success(v1Response));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 목록 조회", description = "상품 그룹 목록을 조회합니다.")
    @GetMapping("/products/group")
    public ResponseEntity<V1ApiResponse<V1PageResponse<ProductGroupDetailV1ApiResponse>>>
            fetchProductGroups(
                    @ModelAttribute @Validated ProductGroupFilterV1ApiRequest filter,
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "20") int size) {
        ProductGroupSearchQuery query = mapper.toQuery(filter, page, size);
        List<ProductGroupSummaryResponse> responses = getProductGroupsUseCase.execute(query);
        long totalElements = getProductGroupsUseCase.count(query);
        List<ProductGroupDetailV1ApiResponse> v1Responses = mapper.toDetailResponses(responses);

        V1PageResponse<ProductGroupDetailV1ApiResponse> pageResponse =
                V1PageResponse.of(v1Responses, page, size, totalElements, null);
        return ResponseEntity.ok(V1ApiResponse.success(pageResponse));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 등록", description = "새로운 상품을 등록합니다.")
    @PostMapping("/product/group")
    public ResponseEntity<V1ApiResponse<CreateProductGroupV1ApiResponse>> registerProduct(
            @Valid @RequestBody CreateProductGroupV1ApiRequest createProductGroup) {

        RegisterFullProductCommand command = mapper.toRegisterCommand(createProductGroup);
        Long productGroupId = registerFullProductUseCase.registerFullProduct(command);
        return ResponseEntity.ok(
                V1ApiResponse.success(
                        new CreateProductGroupV1ApiResponse(
                                productGroupId,
                                createProductGroup.sellerId(),
                                Collections.emptySet())));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 등록", description = "여러 상품을 등록합니다.")
    @PostMapping("/products/group")
    public ResponseEntity<V1ApiResponse<List<Long>>> registerProducts(
            @Valid @RequestBody List<CreateProductGroupV1ApiRequest> createProductGroups) {

        List<Long> productGroupIds =
                createProductGroups.stream()
                        .map(mapper::toRegisterCommand)
                        .map(registerFullProductUseCase::registerFullProduct)
                        .toList();
        return ResponseEntity.ok(V1ApiResponse.success(productGroupIds));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 가격 수정", description = "상품 그룹의 가격을 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/price")
    public ResponseEntity<V1ApiResponse<Long>> updatePrice(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @Valid @RequestBody CreatePriceV1ApiRequest createPrice) {

        UpdateProductPriceCommand command =
                mapper.toUpdatePriceCommand(productGroupId, sellerId, createPrice);
        updateProductPriceUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 가격 일괄 수정", description = "여러 상품 그룹의 가격을 일괄 수정합니다.")
    @PutMapping("/product/group/price/bulk")
    public ResponseEntity<V1ApiResponse<Integer>> updatePriceBulk(
            @RequestBody UpdatePriceContextV1ApiRequest updatePriceContext) {

        List<UpdateProductPriceCommand> commands = mapper.toUpdatePriceCommands(updatePriceContext);
        commands.forEach(updateProductPriceUseCase::execute);
        return ResponseEntity.ok(V1ApiResponse.success(commands.size()));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 카테고리 수정", description = "상품 그룹의 카테고리를 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/category")
    public ResponseEntity<V1ApiResponse<Long>> updateProductGroupCategory(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @Valid @RequestBody UpdateCategoryV1ApiRequest updateCategory) {

        UpdateProductGroupCommand command =
                mapper.toUpdateCategoryCommand(
                        productGroupId, sellerId, updateCategory.categoryId());
        updateProductGroupUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 전시 여부 수정", description = "상품 그룹의 전시 여부를 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/display-yn")
    public ResponseEntity<V1ApiResponse<Long>> updateDisplayYnGroup(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @RequestBody UpdateDisplayYnV1ApiRequest updateDisplayYn) {

        String status = "Y".equalsIgnoreCase(updateDisplayYn.displayYn()) ? "ACTIVE" : "INACTIVE";
        UpdateProductGroupStatusCommand command =
                new UpdateProductGroupStatusCommand(productGroupId, sellerId, status);
        updateProductGroupStatusUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 수정", description = "상품 그룹을 수정합니다.")
    @PutMapping("/product/group/{productGroupId}")
    public ResponseEntity<V1ApiResponse<Long>> updateProductGroup(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @RequestBody UpdateProductGroupV1ApiRequest updateProductGroup) {

        UpdateProductGroupCommand command =
                mapper.toUpdateProductGroupCommand(productGroupId, sellerId, updateProductGroup);
        updateProductGroupUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 개별 상품 전시 여부 수정", description = "개별 상품의 전시 여부를 수정합니다.")
    @PatchMapping("/product/{productId}/display-yn")
    public ResponseEntity<V1ApiResponse<Long>> updateDisplayYnIndividual(
            @PathVariable long productId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @RequestBody UpdateDisplayYnV1ApiRequest updateDisplayYn) {

        boolean displayYn = "Y".equalsIgnoreCase(updateDisplayYn.displayYn());
        UpdateProductDisplayCommand command =
                new UpdateProductDisplayCommand(productId, sellerId, displayYn);
        updateProductDisplayUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(productId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 품절 처리", description = "상품 그룹의 모든 상품을 품절 처리합니다.")
    @PatchMapping("/product/group/{productGroupId}/out-stock")
    public ResponseEntity<V1ApiResponse<Set<ProductFetchV1ApiResponse>>> outOfStock(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId) {

        // 상품그룹의 모든 상품 조회 후 품절 처리
        FullProductResponse fullProduct = getFullProductUseCase.getFullProduct(productGroupId);

        // 각 상품에 대해 품절 처리 실행
        fullProduct
                .products()
                .forEach(
                        product -> {
                            MarkProductOutOfStockCommand command =
                                    new MarkProductOutOfStockCommand(
                                            product.productId(), sellerId, true);
                            markProductOutOfStockUseCase.execute(command);
                        });

        // 품절 처리된 상품 목록 반환
        Set<ProductFetchV1ApiResponse> responses =
                fullProduct.products().stream()
                        .map(
                                p ->
                                        new ProductFetchV1ApiResponse(
                                                p.productId(),
                                                0,
                                                "SOLD_OUT",
                                                buildOptionString(
                                                        p.option1Value(), p.option2Value()),
                                                Collections.emptySet(),
                                                p.additionalPrice()))
                        .collect(Collectors.toSet());

        return ResponseEntity.ok(V1ApiResponse.success(responses));
    }

    private String buildOptionString(String option1, String option2) {
        if (option1 == null && option2 == null) {
            return null;
        }
        if (option2 == null) {
            return option1;
        }
        return option1 + " " + option2;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 삭제", description = "상품 그룹을 삭제합니다.")
    @DeleteMapping("/product/groups")
    public ResponseEntity<V1ApiResponse<List<Long>>> deleteProductGroup(
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @RequestBody DeleteProductGroupV1ApiRequest deleteProductGroup) {

        List<Long> deletedIds =
                deleteProductGroup.productGroupIds().stream()
                        .peek(
                                id -> {
                                    DeleteProductGroupCommand command =
                                            mapper.toDeleteCommand(id, sellerId);
                                    deleteProductGroupUseCase.execute(command);
                                })
                        .toList();
        return ResponseEntity.ok(V1ApiResponse.success(deletedIds));
    }
}
