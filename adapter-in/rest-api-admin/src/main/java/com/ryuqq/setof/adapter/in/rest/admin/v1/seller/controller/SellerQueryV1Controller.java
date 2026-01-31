package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.SellerAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query.SellerFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper.SellerAdminV1ApiMapper;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * SellerQueryV1Controller - 셀러 조회 V1 API.
 *
 * <p>레거시 호환을 위한 V1 셀러 조회 엔드포인트를 제공합니다.
 *
 * <p>API-CTR-001: @RestController 어노테이션 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity&lt;V1ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "셀러 조회 V1", description = "셀러 조회 V1 API (레거시 호환)")
@RestController
public class SellerQueryV1Controller {

    private final GetSellerForAdminUseCase getSellerForAdminUseCase;
    private final SearchSellerByOffsetUseCase searchSellerByOffsetUseCase;
    private final SellerAdminV1ApiMapper mapper;

    public SellerQueryV1Controller(
            GetSellerForAdminUseCase getSellerForAdminUseCase,
            SearchSellerByOffsetUseCase searchSellerByOffsetUseCase,
            SellerAdminV1ApiMapper mapper) {
        this.getSellerForAdminUseCase = getSellerForAdminUseCase;
        this.searchSellerByOffsetUseCase = searchSellerByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 특정 셀러 상세 조회 API.
     *
     * <p>마스터 권한 필요. 셀러 ID로 상세 정보를 조회합니다.
     *
     * @param sellerId 셀러 ID
     * @return 셀러 상세 정보
     * @deprecated V2 API를 사용하세요. GET /api/v2/admin/sellers/{sellerId}
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    @Operation(
            summary = "셀러 상세 조회 (Deprecated)",
            description =
                    "특정 셀러의 상세 정보를 조회합니다. V2 API를 사용하세요: GET /api/v2/admin/sellers/{sellerId}")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원되지 않는 API")
    })
    @GetMapping(SellerAdminV1Endpoints.SELLER_BY_ID)
    public ResponseEntity<V1ApiResponse<SellerDetailV1ApiResponse>> getSeller(
            @Parameter(description = "셀러 ID", required = true)
                    @PathVariable(SellerAdminV1Endpoints.PATH_SELLER_ID)
                    Long sellerId) {
        throw new UnsupportedOperationException(
                "getSeller API is deprecated. Use V2 API: GET /api/v2/admin/sellers/{sellerId}");
    }

    /**
     * 셀러 목록 조회 API (Offset 페이징).
     *
     * <p>마스터 권한 필요. 필터 조건과 페이징으로 셀러 목록을 조회합니다.
     *
     * <p>API-CTR-013: 복합 조건 + Offset 페이징은 search{Bc}ByOffset 네이밍.
     *
     * @param filter 필터 조건
     * @return 셀러 목록 (페이징)
     * @deprecated V2 API를 사용하세요. GET /api/v2/admin/sellers
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    @Operation(
            summary = "셀러 목록 조회 (Deprecated)",
            description = "셀러 목록을 페이지 기반으로 조회합니다. V2 API를 사용하세요: GET /api/v2/admin/sellers")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원되지 않는 API")
    })
    @GetMapping(SellerAdminV1Endpoints.SELLERS)
    public ResponseEntity<V1ApiResponse<PageApiResponse<SellerV1ApiResponse>>>
            searchSellersByOffset(@ModelAttribute @Valid SellerFilterV1ApiRequest filter) {
        throw new UnsupportedOperationException(
                "searchSellersByOffset API is deprecated. Use V2 API: GET /api/v2/admin/sellers");
    }

    /**
     * 사업자등록번호 검증 API.
     *
     * <p>사업자등록번호 유효성 및 중복 여부를 검증합니다.
     *
     * <p><strong>Deprecated</strong>: 이 API는 더 이상 지원되지 않습니다.
     *
     * @return 검증 결과
     * @deprecated 이 API는 더 이상 지원되지 않습니다. 향후 제거될 예정입니다.
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    @Operation(
            summary = "사업자등록번호 검증 (Deprecated)",
            description = "사업자등록번호의 유효성 및 중복 여부를 검증합니다. 더 이상 지원되지 않습니다.",
            deprecated = true)
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원하지 않는 기능")
    })
    @GetMapping(SellerAdminV1Endpoints.SELLERS_BUSINESS_VALIDATION)
    public ResponseEntity<V1ApiResponse<Boolean>> validateBusinessRegistration() {
        throw new UnsupportedOperationException(
                "validateBusinessRegistration API is deprecated and no longer supported");
    }
}
