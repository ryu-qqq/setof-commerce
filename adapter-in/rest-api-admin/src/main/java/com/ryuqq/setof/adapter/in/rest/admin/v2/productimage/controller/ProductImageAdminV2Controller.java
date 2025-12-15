package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response.ProductImageListV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response.ProductImageV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.mapper.ProductImageAdminV2ApiMapper;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.command.DeleteProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductImage Admin V2 Controller
 *
 * <p>상품이미지 관리 API 엔드포인트 (상품그룹 하위 리소스)
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductImage", description = "상품이미지 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductImages.BASE)
@Validated
public class ProductImageAdminV2Controller {

    private final GetProductImageUseCase getProductImageUseCase;
    private final UpdateProductImageUseCase updateProductImageUseCase;
    private final DeleteProductImageUseCase deleteProductImageUseCase;
    private final ProductImageAdminV2ApiMapper mapper;

    public ProductImageAdminV2Controller(
            GetProductImageUseCase getProductImageUseCase,
            UpdateProductImageUseCase updateProductImageUseCase,
            DeleteProductImageUseCase deleteProductImageUseCase,
            ProductImageAdminV2ApiMapper mapper) {
        this.getProductImageUseCase = getProductImageUseCase;
        this.updateProductImageUseCase = updateProductImageUseCase;
        this.deleteProductImageUseCase = deleteProductImageUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품이미지 목록 조회", description = "상품그룹의 모든 이미지를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<ProductImageListV2ApiResponse>> getProductImages(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId) {

        List<ProductImageResponse> responses =
                getProductImageUseCase.getByProductGroupId(productGroupId);

        List<ProductImageV2ApiResponse> items =
                responses.stream().map(ProductImageV2ApiResponse::from).toList();

        ProductImageListV2ApiResponse apiResponse = ProductImageListV2ApiResponse.of(items);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "상품이미지 수정", description = "개별 상품이미지를 수정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "이미지 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.ProductImages.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateProductImage(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Parameter(description = "이미지 ID") @PathVariable Long imageId,
            @Valid @RequestBody UpdateProductImageV2ApiRequest request) {

        UpdateProductImageCommand command = mapper.toUpdateCommand(imageId, request);
        updateProductImageUseCase.update(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "상품이미지 삭제", description = "개별 상품이미지를 삭제합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "이미지 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @DeleteMapping(ApiV2Paths.ProductImages.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Parameter(description = "이미지 ID") @PathVariable Long imageId) {

        deleteProductImageUseCase.delete(imageId);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
