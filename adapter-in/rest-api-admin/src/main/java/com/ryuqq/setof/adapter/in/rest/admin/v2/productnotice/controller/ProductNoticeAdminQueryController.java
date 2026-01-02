package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.response.ProductNoticeV2ApiResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productnotice.port.in.query.GetProductNoticeUseCase;
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
 * ProductNotice Admin Query Controller
 *
 * <p>상품고시 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin ProductNotice", description = "상품고시 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductNotices.BASE)
@Validated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductNoticeAdminQueryController {

    private final GetProductNoticeUseCase getProductNoticeUseCase;

    public ProductNoticeAdminQueryController(GetProductNoticeUseCase getProductNoticeUseCase) {
        this.getProductNoticeUseCase = getProductNoticeUseCase;
    }

    @Operation(summary = "상품고시 조회", description = "상품그룹 ID로 상품고시를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품고시 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<ProductNoticeV2ApiResponse>> getProductNotice(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId) {

        ProductNoticeResponse response =
                getProductNoticeUseCase
                        .findByProductGroupId(productGroupId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "상품고시를 찾을 수 없습니다. productGroupId="
                                                        + productGroupId));

        ProductNoticeV2ApiResponse apiResponse = ProductNoticeV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
