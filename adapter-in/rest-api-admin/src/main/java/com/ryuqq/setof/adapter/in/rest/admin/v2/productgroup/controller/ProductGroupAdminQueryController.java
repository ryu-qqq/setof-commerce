package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.query.ProductGroupSearchV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupListV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper.ProductGroupAdminV2ApiMapper;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductGroup Admin Query Controller
 *
 * <p>상품그룹 조회 API 엔드포인트 (CQRS Query 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>GET 메서드만 포함
 *   <li>Query DTO는 @ModelAttribute로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductGroup", description = "상품그룹 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductGroups.BASE)
@Validated
public class ProductGroupAdminQueryController {

    private final GetProductGroupUseCase getProductGroupUseCase;
    private final GetProductGroupsUseCase getProductGroupsUseCase;
    private final ProductGroupAdminV2ApiMapper mapper;

    public ProductGroupAdminQueryController(
            GetProductGroupUseCase getProductGroupUseCase,
            GetProductGroupsUseCase getProductGroupsUseCase,
            ProductGroupAdminV2ApiMapper mapper) {
        this.getProductGroupUseCase = getProductGroupUseCase;
        this.getProductGroupsUseCase = getProductGroupsUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품그룹 단일 조회", description = "상품그룹 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품그룹 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping(ApiV2Paths.ProductGroups.ID_PATH)
    public ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> getProductGroup(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId) {

        ProductGroupResponse response = getProductGroupUseCase.execute(productGroupId);
        ProductGroupV2ApiResponse apiResponse = ProductGroupV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "상품그룹 목록 조회", description = "검색 조건에 따라 상품그룹 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping
    public ResponseEntity<ApiResponse<ProductGroupListV2ApiResponse>> getProductGroups(
            @Valid @ModelAttribute ProductGroupSearchV2ApiRequest request) {

        ProductGroupSearchQuery query = mapper.toSearchQuery(request);

        List<ProductGroupSummaryResponse> responses = getProductGroupsUseCase.execute(query);
        long totalCount = getProductGroupsUseCase.count(query);

        List<ProductGroupSummaryV2ApiResponse> items =
                responses.stream().map(ProductGroupSummaryV2ApiResponse::from).toList();

        ProductGroupListV2ApiResponse apiResponse =
                ProductGroupListV2ApiResponse.of(
                        items, request.pageNumber(), request.pageSize(), totalCount);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
