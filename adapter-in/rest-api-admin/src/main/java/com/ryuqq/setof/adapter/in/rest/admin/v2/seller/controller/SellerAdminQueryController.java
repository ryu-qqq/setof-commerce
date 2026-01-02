package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SellerSearchV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerPageV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerAdminV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
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
 * Seller Admin Query Controller
 *
 * <p>셀러 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin Seller", description = "셀러 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Sellers.BASE)
@Validated
public class SellerAdminQueryController {

    private final GetSellerUseCase getSellerUseCase;
    private final GetSellersUseCase getSellersUseCase;
    private final SellerAdminV2ApiMapper mapper;

    public SellerAdminQueryController(
            GetSellerUseCase getSellerUseCase,
            GetSellersUseCase getSellersUseCase,
            SellerAdminV2ApiMapper mapper) {
        this.getSellerUseCase = getSellerUseCase;
        this.getSellersUseCase = getSellersUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "셀러 목록 조회", description = "검색 조건에 맞는 셀러 목록을 페이징하여 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping
    public ResponseEntity<ApiResponse<SellerPageV2ApiResponse>> searchSellers(
            @Valid @ModelAttribute SellerSearchV2ApiRequest request) {

        SellerSearchQuery query = mapper.toSearchQuery(request);
        PageResponse<SellerSummaryResponse> pageResponse = getSellersUseCase.execute(query);

        List<SellerSummaryV2ApiResponse> apiResponses =
                pageResponse.content().stream().map(SellerSummaryV2ApiResponse::from).toList();

        SellerPageV2ApiResponse response =
                SellerPageV2ApiResponse.of(
                        apiResponses,
                        pageResponse.page(),
                        pageResponse.size(),
                        pageResponse.totalElements());

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    @Operation(summary = "셀러 상세 조회", description = "셀러 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@sellerAccess.canAccess(#sellerId)")
    @GetMapping(ApiV2Paths.Sellers.ID_PATH)
    public ResponseEntity<ApiResponse<SellerV2ApiResponse>> getSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        SellerResponse response = getSellerUseCase.execute(sellerId);
        SellerV2ApiResponse apiResponse = SellerV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
