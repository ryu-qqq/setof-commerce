package com.ryuqq.setof.adapter.in.rest.admin.v1.product.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateDeliveryNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateOptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreatePriceV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductImageV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateRefundNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.DeleteProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateCategoryV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdatePriceContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductDescriptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductStockV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.query.ProductGroupFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.CreateProductGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response.ProductGroupFetchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.mapper.ProductAdminV1ApiMapper;
import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.command.DeleteProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
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
public class ProductV1Controller {

    private final GetFullProductUseCase getFullProductUseCase;
    private final GetProductGroupsUseCase getProductGroupsUseCase;
    private final RegisterFullProductUseCase registerFullProductUseCase;
    private final DeleteProductGroupUseCase deleteProductGroupUseCase;
    private final UpdateProductGroupUseCase updateProductGroupUseCase;
    private final SetStockUseCase setStockUseCase;
    private final ProductAdminV1ApiMapper mapper;

    public ProductV1Controller(
            GetFullProductUseCase getFullProductUseCase,
            GetProductGroupsUseCase getProductGroupsUseCase,
            RegisterFullProductUseCase registerFullProductUseCase,
            DeleteProductGroupUseCase deleteProductGroupUseCase,
            UpdateProductGroupUseCase updateProductGroupUseCase,
            SetStockUseCase setStockUseCase,
            ProductAdminV1ApiMapper mapper) {
        this.getFullProductUseCase = getFullProductUseCase;
        this.getProductGroupsUseCase = getProductGroupsUseCase;
        this.registerFullProductUseCase = registerFullProductUseCase;
        this.deleteProductGroupUseCase = deleteProductGroupUseCase;
        this.updateProductGroupUseCase = updateProductGroupUseCase;
        this.setStockUseCase = setStockUseCase;
        this.mapper = mapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 조회", description = "특정 상품 그룹을 조회합니다.")
    @GetMapping("/product/group/{productGroupId}")
    public ResponseEntity<ApiResponse<ProductGroupFetchV1ApiResponse>> fetchProductGroup(
            @PathVariable long productGroupId) {
        FullProductResponse response = getFullProductUseCase.getFullProduct(productGroupId);
        ProductGroupFetchV1ApiResponse v1Response = mapper.toFetchResponse(response);
        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 목록 조회", description = "상품 그룹 목록을 조회합니다.")
    @GetMapping("/products/group")
    public ResponseEntity<ApiResponse<PageApiResponse<ProductGroupDetailV1ApiResponse>>>
            fetchProductGroups(
                    @ModelAttribute @Validated ProductGroupFilterV1ApiRequest filter,
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "20") int size) {
        ProductGroupSearchQuery query = mapper.toQuery(filter, page, size);
        List<ProductGroupSummaryResponse> responses = getProductGroupsUseCase.execute(query);
        long totalElements = getProductGroupsUseCase.count(query);
        List<ProductGroupDetailV1ApiResponse> v1Responses = mapper.toDetailResponses(responses);

        int totalPages = (int) Math.ceil((double) totalElements / size);
        PageApiResponse<ProductGroupDetailV1ApiResponse> pageResponse =
                new PageApiResponse<>(
                        v1Responses,
                        page,
                        size,
                        totalElements,
                        totalPages,
                        page == 0,
                        page >= totalPages - 1);
        return ResponseEntity.ok(ApiResponse.ofSuccess(pageResponse));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 등록", description = "새로운 상품을 등록합니다.")
    @PostMapping("/product/group")
    public ResponseEntity<ApiResponse<CreateProductGroupV1ApiResponse>> registerProduct(
            @RequestHeader(value = "X-Product-Id", required = false) String externalProductId,
            @Valid @RequestBody CreateProductGroupV1ApiRequest createProductGroup) {

        RegisterFullProductCommand command = mapper.toRegisterCommand(createProductGroup);
        Long productGroupId = registerFullProductUseCase.registerFullProduct(command);
        return ResponseEntity.ok(
                ApiResponse.ofSuccess(
                        new CreateProductGroupV1ApiResponse(
                                productGroupId,
                                createProductGroup.sellerId(),
                                Collections.emptySet())));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 등록", description = "여러 상품을 등록합니다.")
    @PostMapping("/products/group")
    public ResponseEntity<ApiResponse<List<Long>>> registerProducts(
            @RequestHeader(value = "X-Product-Id", required = false) String externalProductId,
            @Valid @RequestBody List<CreateProductGroupV1ApiRequest> createProductGroups) {

        List<Long> productGroupIds =
                createProductGroups.stream()
                        .map(mapper::toRegisterCommand)
                        .map(registerFullProductUseCase::registerFullProduct)
                        .toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(productGroupIds));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 고지 수정", description = "상품 고지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/notice")
    public ResponseEntity<ApiResponse<Long>> updateProductNotice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreateProductNoticeV1ApiRequest createProductNotice) {

        throw new UnsupportedOperationException("상품 고지 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송 고지 수정", description = "배송 고지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/notice/delivery")
    public ResponseEntity<ApiResponse<Long>> updateProductDeliveryNotice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreateDeliveryNoticeV1ApiRequest deliveryNotice) {

        throw new UnsupportedOperationException("배송 고지 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 환불 고지 수정", description = "환불 고지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/notice/refund")
    public ResponseEntity<ApiResponse<Long>> updateProductRefundNotice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreateRefundNoticeV1ApiRequest refundNotice) {

        throw new UnsupportedOperationException("환불 고지 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 이미지 수정", description = "상품 이미지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/images")
    public ResponseEntity<ApiResponse<Long>> updateProductImages(
            @PathVariable long productGroupId,
            @Valid @RequestBody List<CreateProductImageV1ApiRequest> createProductImages) {

        throw new UnsupportedOperationException("상품 이미지 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 상세 설명 수정", description = "상품 상세 설명을 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/detailDescription")
    public ResponseEntity<ApiResponse<Long>> updateProductDescription(
            @PathVariable long productGroupId,
            @RequestBody UpdateProductDescriptionV1ApiRequest updateProductDescription) {

        throw new UnsupportedOperationException("상품 상세 설명 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 카테고리 수정", description = "상품 그룹의 카테고리를 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/category")
    public ResponseEntity<ApiResponse<Long>> updateProductGroupCategory(
            @PathVariable long productGroupId,
            @Valid @RequestBody UpdateCategoryV1ApiRequest updateCategory) {

        throw new UnsupportedOperationException("상품 그룹 카테고리 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 가격 수정", description = "상품 그룹의 가격을 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/price")
    public ResponseEntity<ApiResponse<Long>> updatePrice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreatePriceV1ApiRequest createPrice) {

        throw new UnsupportedOperationException("가격 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 가격 일괄 수정", description = "여러 상품 그룹의 가격을 일괄 수정합니다.")
    @PutMapping("/product/group/price/bulk")
    public ResponseEntity<ApiResponse<Integer>> updatePrice(
            @RequestBody UpdatePriceContextV1ApiRequest updatePriceContext) {

        throw new UnsupportedOperationException("가격 일괄 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 전시 여부 수정", description = "상품 그룹의 전시 여부를 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/display-yn")
    public ResponseEntity<ApiResponse<Long>> updateDisplayYnGroup(
            @PathVariable long productGroupId,
            @RequestBody UpdateDisplayYnV1ApiRequest updateDisplayYn) {

        throw new UnsupportedOperationException("상품 그룹 전시 여부 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 수정", description = "상품 그룹을 수정합니다.")
    @PutMapping("/product/group/{productGroupId}")
    public ResponseEntity<ApiResponse<Long>> updateProductGroup(
            @PathVariable long productGroupId,
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @RequestBody UpdateProductGroupV1ApiRequest updateProductGroup) {

        UpdateProductGroupCommand command =
                mapper.toUpdateProductGroupCommand(productGroupId, sellerId, updateProductGroup);
        updateProductGroupUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(productGroupId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 개별 상품 전시 여부 수정", description = "개별 상품의 전시 여부를 수정합니다.")
    @PatchMapping("/product/{productId}/display-yn")
    public ResponseEntity<ApiResponse<Long>> updateDisplayYnIndividual(
            @PathVariable long productId,
            @RequestBody UpdateDisplayYnV1ApiRequest updateDisplayYn) {

        throw new UnsupportedOperationException("개별 상품 전시 여부 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 품절 처리", description = "상품 그룹의 모든 상품을 품절 처리합니다.")
    @PatchMapping("/product/group/{productGroupId}/out-stock")
    public ResponseEntity<ApiResponse<Set<ProductFetchV1ApiResponse>>> outOfStock(
            @PathVariable long productGroupId) {

        throw new UnsupportedOperationException("상품 그룹 품절 처리 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 옵션 수정", description = "상품 그룹의 옵션을 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/option")
    public ResponseEntity<ApiResponse<Set<ProductFetchV1ApiResponse>>> updateProductOption(
            @PathVariable long productGroupId,
            @Valid @RequestBody List<CreateOptionV1ApiRequest> options) {

        throw new UnsupportedOperationException("상품 옵션 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 삭제", description = "상품 그룹을 삭제합니다.")
    @DeleteMapping("/product/groups")
    public ResponseEntity<ApiResponse<List<Long>>> deleteProductGroup(
            @RequestHeader(value = "X-Seller-Id", required = false) Long sellerId,
            @RequestBody DeleteProductGroupV1ApiRequest deleteProductGroup) {

        List<Long> deletedIds =
                deleteProductGroup.productGroupIds().stream()
                        .map(
                                id -> {
                                    DeleteProductGroupCommand command =
                                            mapper.toDeleteCommand(id, sellerId);
                                    deleteProductGroupUseCase.execute(command);
                                    return id;
                                })
                        .toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(deletedIds));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 개별 상품 재고 수정", description = "개별 상품의 재고를 수정합니다.")
    @PatchMapping("/product/{productId}/stock")
    public ResponseEntity<ApiResponse<Set<ProductFetchV1ApiResponse>>> updateProductStock(
            @PathVariable long productId,
            @Valid @RequestBody UpdateProductStockV1ApiRequest updateProductStock) {

        SetStockCommand command = mapper.toSetStockCommand(updateProductStock);
        setStockUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(Collections.emptySet()));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 그룹 재고 수정", description = "상품 그룹의 여러 상품 재고를 수정합니다.")
    @PatchMapping("/product/group/{productGroupId}/stock")
    public ResponseEntity<ApiResponse<Set<ProductFetchV1ApiResponse>>> updateProductStocks(
            @PathVariable long productGroupId,
            @Valid @RequestBody List<UpdateProductStockV1ApiRequest> updateProductStocks) {

        updateProductStocks.forEach(
                request -> {
                    SetStockCommand command = mapper.toSetStockCommand(request);
                    setStockUseCase.execute(command);
                });
        return ResponseEntity.ok(ApiResponse.ofSuccess(Collections.emptySet()));
    }
}
