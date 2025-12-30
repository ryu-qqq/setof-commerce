package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.response.ProductDescriptionV2ApiResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductDescription Admin Query Controller
 *
 * <p>상품설명 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin ProductDescription", description = "상품설명 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductDescriptions.BASE)
@Validated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductDescriptionAdminQueryController {

    private final GetProductDescriptionUseCase getProductDescriptionUseCase;

    public ProductDescriptionAdminQueryController(
            GetProductDescriptionUseCase getProductDescriptionUseCase) {
        this.getProductDescriptionUseCase = getProductDescriptionUseCase;
    }

    @Operation(summary = "상품설명 조회", description = "상품그룹 ID로 상품설명을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품설명 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<ProductDescriptionV2ApiResponse>> getProductDescription(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId) {

        ProductDescriptionResponse response =
                getProductDescriptionUseCase
                        .findByProductGroupId(ProductGroupId.of(productGroupId))
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "상품설명을 찾을 수 없습니다. productGroupId="
                                                        + productGroupId));

        ProductDescriptionV2ApiResponse apiResponse =
                ProductDescriptionV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
