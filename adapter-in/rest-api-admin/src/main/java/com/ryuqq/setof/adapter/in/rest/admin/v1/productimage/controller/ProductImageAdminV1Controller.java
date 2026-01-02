package com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.dto.command.CreateProductImageV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.dto.command.UpdateProductDescriptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.mapper.ProductImageAdminV1ApiMapper;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Product Image Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Product Image 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Product Image (Legacy V1)", description = "레거시 Product Image API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductImageAdminV1Controller {

    private final ProductImageAdminV1ApiMapper productImageAdminV1ApiMapper;
    private final UpdateProductImageUseCase updateProductImageUseCase;
    private final GetProductImageUseCase getProductImageUseCase;
    private final GetProductDescriptionUseCase getProductDescriptionUseCase;
    private final UpdateProductDescriptionUseCase updateProductDescriptionUseCase;

    public ProductImageAdminV1Controller(
            ProductImageAdminV1ApiMapper productImageAdminV1ApiMapper,
            UpdateProductImageUseCase updateProductImageUseCase,
            GetProductImageUseCase getProductImageUseCase,
            GetProductDescriptionUseCase getProductDescriptionUseCase,
            UpdateProductDescriptionUseCase updateProductDescriptionUseCase) {
        this.productImageAdminV1ApiMapper = productImageAdminV1ApiMapper;
        this.updateProductImageUseCase = updateProductImageUseCase;
        this.getProductImageUseCase = getProductImageUseCase;
        this.getProductDescriptionUseCase = getProductDescriptionUseCase;
        this.updateProductDescriptionUseCase = updateProductDescriptionUseCase;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 이미지 수정", description = "상품 이미지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/images")
    public ResponseEntity<V1ApiResponse<Long>> updateProductImages(
            @PathVariable long productGroupId,
            @Valid @RequestBody List<CreateProductImageV1ApiRequest> createProductImages) {

        List<ProductImageResponse> existingImages =
                getProductImageUseCase.getByProductGroupId(ProductGroupId.of(productGroupId));
        List<Long> existingImageIds =
                existingImages.stream().map(ProductImageResponse::id).toList();

        List<UpdateProductImageCommand> commands =
                productImageAdminV1ApiMapper.toUpdateImageCommands(
                        existingImageIds, createProductImages);

        commands.forEach(updateProductImageUseCase::update);

        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 상세 설명 수정", description = "상품 상세 설명을 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/detailDescription")
    public ResponseEntity<V1ApiResponse<Long>> updateProductDescription(
            @PathVariable long productGroupId,
            @RequestBody UpdateProductDescriptionV1ApiRequest updateProductDescription) {

        ProductDescriptionResponse existingDescription =
                getProductDescriptionUseCase
                        .findByProductGroupId(ProductGroupId.of(productGroupId))
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "상품 상세 설명을 찾을 수 없습니다. productGroupId: "
                                                        + productGroupId));

        UpdateProductDescriptionCommand command =
                productImageAdminV1ApiMapper.toUpdateDescriptionCommand(
                        existingDescription.productDescriptionId(), updateProductDescription);

        updateProductDescriptionUseCase.execute(command);

        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }
}
