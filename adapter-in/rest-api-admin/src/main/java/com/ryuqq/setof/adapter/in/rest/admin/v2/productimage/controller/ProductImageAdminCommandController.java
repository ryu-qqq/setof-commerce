package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.mapper.ProductImageAdminV2ApiMapper;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.port.in.command.DeleteProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductImage Admin Command Controller
 *
 * <p>상품이미지 수정/삭제 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductImage", description = "상품이미지 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductImages.BASE)
@Validated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductImageAdminCommandController {

    private final UpdateProductImageUseCase updateProductImageUseCase;
    private final DeleteProductImageUseCase deleteProductImageUseCase;
    private final ProductImageAdminV2ApiMapper mapper;

    public ProductImageAdminCommandController(
            UpdateProductImageUseCase updateProductImageUseCase,
            DeleteProductImageUseCase deleteProductImageUseCase,
            ProductImageAdminV2ApiMapper mapper) {
        this.updateProductImageUseCase = updateProductImageUseCase;
        this.deleteProductImageUseCase = deleteProductImageUseCase;
        this.mapper = mapper;
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

    @Operation(summary = "상품이미지 삭제", description = "개별 상품이미지를 삭제합니다 (소프트 삭제).")
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
    @PatchMapping(ApiV2Paths.ProductImages.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Parameter(description = "이미지 ID") @PathVariable Long imageId) {

        deleteProductImageUseCase.delete(imageId);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
