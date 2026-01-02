package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.response.ProductStockV2ApiResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import com.ryuqq.setof.application.productstock.port.in.query.GetProductStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductStock Admin Query Controller
 *
 * <p>재고 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin ProductStock", description = "재고 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductStocks.BASE)
@Validated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductStockAdminQueryController {

    private final GetProductStockUseCase getProductStockUseCase;

    public ProductStockAdminQueryController(GetProductStockUseCase getProductStockUseCase) {
        this.getProductStockUseCase = getProductStockUseCase;
    }

    @Operation(summary = "단일 상품 재고 조회", description = "상품 ID로 재고 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "재고 정보 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.ProductStocks.SINGLE_STOCK_PATH)
    public ResponseEntity<ApiResponse<ProductStockV2ApiResponse>> getStock(
            @Parameter(description = "상품 ID") @PathVariable Long productId) {

        ProductStockResponse response = getProductStockUseCase.execute(productId);
        ProductStockV2ApiResponse apiResponse = ProductStockV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "상품 그룹 내 재고 목록 조회", description = "상품 그룹에 속한 모든 상품의 재고 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.ProductStocks.GROUP_STOCKS_PATH)
    public ResponseEntity<ApiResponse<List<ProductStockV2ApiResponse>>> getGroupStocks(
            @Parameter(description = "상품 그룹 ID") @PathVariable Long productGroupId) {

        // TODO: ProductGroup에서 productIds 조회 후 일괄 조회
        // 현재는 productGroupId를 productIds로 변환하는 로직이 필요
        // GetProductsByProductGroupIdUseCase 필요
        throw new UnsupportedOperationException("ProductGroup → ProductIds 조회 UseCase 연동 필요");
    }
}
