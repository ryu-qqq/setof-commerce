package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response.ProductImageListV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response.ProductImageV2ApiResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductImage Admin Query Controller
 *
 * <p>상품이미지 조회 API 엔드포인트 (CQRS Query 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>GET 메서드만 포함
 *   <li>비즈니스 로직은 UseCase에 위임
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
public class ProductImageAdminQueryController {

    private final GetProductImageUseCase getProductImageUseCase;

    public ProductImageAdminQueryController(GetProductImageUseCase getProductImageUseCase) {
        this.getProductImageUseCase = getProductImageUseCase;
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
}
