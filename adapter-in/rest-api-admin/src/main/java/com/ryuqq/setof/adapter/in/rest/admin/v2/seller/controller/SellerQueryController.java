package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.SellerAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SearchSellersApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerQueryApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SellerQueryController - 셀러 조회 API.
 *
 * <p>셀러 조회 엔드포인트를 제공합니다.
 *
 * <p>API-CTR-001: @RestController 어노테이션 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * <p>API-CTR-012: URL 경로 소문자 + 복수형 (/sellers).
 *
 * <p>API-CTR-011: List 직접 반환 금지 -&gt; PageApiResponse 페이징 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "셀러 조회", description = "셀러 조회 API")
@RestController
@RequestMapping(SellerAdminEndpoints.SELLERS)
public class SellerQueryController {

    private final GetSellerForAdminUseCase getSellerForAdminUseCase;
    private final SearchSellerByOffsetUseCase searchSellerByOffsetUseCase;
    private final SellerQueryApiMapper mapper;

    /**
     * SellerQueryController 생성자.
     *
     * @param getSellerForAdminUseCase 셀러 단일 조회 UseCase
     * @param searchSellerByOffsetUseCase 셀러 목록 검색 UseCase
     * @param mapper Query API 매퍼
     */
    public SellerQueryController(
            GetSellerForAdminUseCase getSellerForAdminUseCase,
            SearchSellerByOffsetUseCase searchSellerByOffsetUseCase,
            SellerQueryApiMapper mapper) {
        this.getSellerForAdminUseCase = getSellerForAdminUseCase;
        this.searchSellerByOffsetUseCase = searchSellerByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 셀러 상세 조회 API.
     *
     * <p>셀러 ID로 셀러 상세 정보를 조회합니다.
     *
     * <p>API-CTR-007: Controller 비즈니스 로직 금지 → Mapper에서 변환 처리.
     *
     * @param sellerId 셀러 ID
     * @return 셀러 상세 응답
     */
    @Operation(summary = "셀러 상세 조회", description = "셀러 ID로 셀러 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "셀러를 찾을 수 없음")
    })
    @GetMapping(SellerAdminEndpoints.ID)
    public ResponseEntity<ApiResponse<SellerDetailApiResponse>> getSeller(
            @Parameter(description = "셀러 ID", required = true)
                    @PathVariable(SellerAdminEndpoints.PATH_SELLER_ID)
                    Long sellerId) {

        SellerCompositeResult result = getSellerForAdminUseCase.execute(sellerId);
        SellerDetailApiResponse response = mapper.toDetailResponse(result);

        return ResponseEntity.ok(ApiResponse.of(response));
    }

    /**
     * 셀러 목록 조회 API.
     *
     * <p>셀러 목록을 페이지 기반으로 조회합니다.
     *
     * <p>API-CTR-007: Controller 비즈니스 로직 금지 → Mapper에서 변환 처리.
     *
     * @param request 조회 요청 DTO (페이지 기반, 필터 포함)
     * @return 셀러 페이지 목록
     */
    @Operation(summary = "셀러 목록 조회", description = "셀러 목록을 페이지 기반으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageApiResponse<SellerApiResponse>>> search(
            @Valid SearchSellersApiRequest request) {

        SellerSearchParams searchParams = mapper.toSearchParams(request);
        SellerPageResult pageResult = searchSellerByOffsetUseCase.execute(searchParams);
        PageApiResponse<SellerApiResponse> response = mapper.toPageResponse(pageResult);

        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
