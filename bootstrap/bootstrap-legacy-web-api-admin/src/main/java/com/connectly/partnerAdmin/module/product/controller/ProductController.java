package com.connectly.partnerAdmin.module.product.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupInfoDto;
import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroupResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.dto.query.DeleteProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateCategory;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateDisplayYn;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductDescription;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductStock;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;
import com.connectly.partnerAdmin.module.product.request.UpdatePriceContext;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupUpdateService;
import com.connectly.partnerAdmin.module.product.service.group.ProductRegistrationService;
import com.connectly.partnerAdmin.module.product.service.stock.ProductStockUpdateService;
import com.connectly.partnerAdmin.module.product.service.stock.ProductUpdateService;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;

import lombok.RequiredArgsConstructor;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ProductController {

    private final ProductGroupFetchService productGroupFetchService;
    private final ProductRegistrationService productRegistrationService;
    private final ProductGroupUpdateService productGroupUpdateService;
    private final ProductUpdateService productUpdateService;
    private final ProductStockUpdateService productStockUpdateService;

    @GetMapping("/product/group/{productGroupId}")
    public ResponseEntity<ApiResponse<ProductGroupFetchResponse>> fetchProductGroup(@PathVariable long productGroupId){
        return ResponseEntity.ok(ApiResponse.success(productGroupFetchService.fetchProductGroup(productGroupId)));
    }

    @GetMapping("/product/group/uuid/{externalProductUuId}")
    public ResponseEntity<ApiResponse<List<ProductGroupInfoDto>>> fetchProductGroup(@PathVariable String externalProductUuId){
        return ResponseEntity.ok(ApiResponse.success(productGroupFetchService.fetchProductGroup(externalProductUuId)));
    }

    @GetMapping("/products/group")
    public ResponseEntity<ApiResponse<CustomPageable<ProductGroupDetailResponse>>> fetchProductGroups(@ModelAttribute @Validated ProductGroupFilter filter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(productGroupFetchService.fetchProductGroups(filter, pageable)));
    }

    @PostMapping("/product/group")
    public ResponseEntity<ApiResponse<CreateProductGroupResponse>> registerProduct(
            @RequestHeader(value = "X-Product-Id", required = false) String externalProductId,
            @RequestBody @Validated CreateProductGroup createProductGroup){
        String productId = resolveExternalProductId(externalProductId);
        return ResponseEntity.ok(ApiResponse.success(productRegistrationService.registerProduct(productId, createProductGroup)));
    }

    @PostMapping("/products/group")
    public ResponseEntity<ApiResponse<List<Long>>> registerProducts(
            @RequestHeader(value = "X-Product-Id", required = false) String externalProductId,
            @RequestBody @Validated List<CreateProductGroup> createProductGroups){
        String productId = resolveExternalProductId(externalProductId);
        return ResponseEntity.ok(ApiResponse.success(productRegistrationService.registerProducts(productId, createProductGroups)));
    }

    private String resolveExternalProductId(String externalProductId) {
        if (externalProductId == null || externalProductId.isBlank()) {
            return java.util.UUID.randomUUID().toString();
        }
        return externalProductId;
    }

    @PutMapping("/product/group/{productGroupId}/notice")
    public ResponseEntity<ApiResponse<Long>> updateProductNotice(@PathVariable long productGroupId, @RequestBody @Validated CreateProductNotice createProductNotice){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateProductNotice(productGroupId, createProductNotice)));
    }

    @PutMapping("/product/group/{productGroupId}/notice/delivery")
    public ResponseEntity<ApiResponse<Long>> updateProductDeliveryNotice(@PathVariable long productGroupId, @RequestBody @Validated CreateDeliveryNotice deliveryNotice){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateProductDeliveryNotice(productGroupId, deliveryNotice)));
    }

    @PutMapping("/product/group/{productGroupId}/notice/refund")
    public ResponseEntity<ApiResponse<Long>> updateProductRefundNotice(@PathVariable long productGroupId, @RequestBody @Validated CreateRefundNotice refundNotice){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateProductRefundNotice(productGroupId, refundNotice)));
    }

    @PutMapping("/product/group/{productGroupId}/images")
    public ResponseEntity<ApiResponse<Long>> updateProductImages(@PathVariable long productGroupId, @RequestBody @Validated List<CreateProductImage> createProductImages){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateProductImage(productGroupId, createProductImages)));
    }

    @PutMapping("/product/group/{productGroupId}/detailDescription")
    public ResponseEntity<ApiResponse<Long>> updateProductDescription(@PathVariable long productGroupId, @RequestBody UpdateProductDescription updateProductDescription){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateDetailDescription(productGroupId, updateProductDescription)));
    }

    @PatchMapping("/product/group/{productGroupId}/category")
    public ResponseEntity<ApiResponse<Long>> updateProductGroupCategory(@PathVariable long productGroupId, @RequestBody @Validated UpdateCategory updateCategory){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateProductGroupCategory(productGroupId, updateCategory)));
    }

    @PatchMapping("/product/group/{productGroupId}/price")
    public ResponseEntity<ApiResponse<Long>> updatePrice(@PathVariable long productGroupId, @RequestBody @Validated CreatePrice createPrice){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updatePrice(productGroupId, createPrice)));
    }

    @PutMapping("/product/group/price/bulk")
    public ResponseEntity<ApiResponse<Integer>> updatePrice(@RequestBody UpdatePriceContext updatePriceContext){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updatePrice(updatePriceContext)));
    }

    @PatchMapping("/product/group/{productGroupId}/display-yn")
    public ResponseEntity<ApiResponse<Long>> updateDisplayYnGroup(@PathVariable long productGroupId, @RequestBody UpdateDisplayYn updateDisplayYn){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateGroupDisplayYn(productGroupId, updateDisplayYn)));
    }

    @PutMapping("/product/group/{productGroupId}")
    public ResponseEntity<ApiResponse<Long>> updateProductGroup(@PathVariable long productGroupId, @RequestBody UpdateProductGroup updateProductGroup){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.updateProductGroup(productGroupId, updateProductGroup)));
    }

    @PatchMapping("/product/{productId}/display-yn")
    public ResponseEntity<ApiResponse<Long>> updateDisplayYnIndividual(@PathVariable long productId, @RequestBody UpdateDisplayYn updateDisplayYn){
        return ResponseEntity.ok(ApiResponse.success(productUpdateService.updateIndividualDisplayYn(productId, updateDisplayYn)));
    }


    @PatchMapping("/product/group/{productGroupId}/out-stock")
    public ResponseEntity<ApiResponse<Set<ProductFetchResponse>>> outOfStock(@PathVariable long productGroupId){
        return ResponseEntity.ok(ApiResponse.success(productStockUpdateService.outOfStock(productGroupId)));
    }

    @PutMapping("/product/group/{productGroupId}/option")
    public ResponseEntity<ApiResponse<Set<ProductFetchResponse>>> updateProductOption(@PathVariable long productGroupId, @RequestBody @Validated List<CreateOption> options){
        return ResponseEntity.ok(ApiResponse.success(productUpdateService.updateProductAndStock(productGroupId, options)));
    }

    @Deprecated
    @PreAuthorize(HAS_AUTHORITY_MASTER)
    @DeleteMapping("/product/groups")
    public ResponseEntity<ApiResponse<List<Long>>> deleteProductGroup(@RequestBody DeleteProductGroup deleteProductGroup){
        return ResponseEntity.ok(ApiResponse.success(productGroupUpdateService.deleteProductGroups(deleteProductGroup)));
    }

    @Deprecated
    @PatchMapping("/product/{productId}/stock")
    public ResponseEntity<ApiResponse<Set<ProductFetchResponse>>> updateProductStock(@PathVariable long productId, @RequestBody @Validated UpdateProductStock updateProductStock){
        return ResponseEntity.ok(ApiResponse.success(productStockUpdateService.updateProductStock(productId, updateProductStock)));
    }

    @Deprecated
    @PatchMapping("/product/group/{productGroupId}/stock")
    public ResponseEntity<ApiResponse<Set<ProductFetchResponse>>> updateProductStocks(@PathVariable long productGroupId, @RequestBody @Validated List<UpdateProductStock> updateProductStocks){
        return ResponseEntity.ok(ApiResponse.success(productStockUpdateService.updateProductStocks(productGroupId, updateProductStocks)));
    }


}

