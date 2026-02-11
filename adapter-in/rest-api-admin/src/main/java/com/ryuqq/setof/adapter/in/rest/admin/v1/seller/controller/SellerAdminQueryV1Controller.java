package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.SellerAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.BusinessValidationV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SearchSellersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper.SellerAdminV1ApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import com.ryuqq.setof.application.seller.port.in.query.ValidateBusinessRegistrationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SellerAdminQueryV1Controller - 셀러 조회 Admin V1 API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>레거시 SellerController.fetchSellers / fetchSellerById / fetchBusinessValidation 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "셀러 조회 Admin V1", description = "셀러 조회 Admin V1 API")
@RestController
@RequestMapping(SellerAdminV1Endpoints.SELLERS)
public class SellerAdminQueryV1Controller {

    private final SearchSellerByOffsetUseCase searchSellerByOffsetUseCase;
    private final GetSellerForAdminUseCase getSellerForAdminUseCase;
    private final ValidateBusinessRegistrationUseCase validateBusinessRegistrationUseCase;
    private final SellerAdminV1ApiMapper mapper;

    public SellerAdminQueryV1Controller(
            SearchSellerByOffsetUseCase searchSellerByOffsetUseCase,
            GetSellerForAdminUseCase getSellerForAdminUseCase,
            ValidateBusinessRegistrationUseCase validateBusinessRegistrationUseCase,
            SellerAdminV1ApiMapper mapper) {
        this.searchSellerByOffsetUseCase = searchSellerByOffsetUseCase;
        this.getSellerForAdminUseCase = getSellerForAdminUseCase;
        this.validateBusinessRegistrationUseCase = validateBusinessRegistrationUseCase;
        this.mapper = mapper;
    }

    /**
     * 셀러 목록 검색 API.
     *
     * <p>GET /api/v1/sellers - searchKeyword/searchWord로 필터링, Offset 페이징.
     *
     * @param request 검색 요청 (searchKeyword, searchWord, siteIds, status, page, size)
     * @return 셀러 목록 (페이징)
     */
    @Operation(summary = "셀러 목록 검색", description = "셀러명/ID로 검색하고 페이징된 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<SellerV1ApiResponse>>>
            fetchSellers(@ParameterObject @Valid SearchSellersV1ApiRequest request) {
        SellerSearchParams params = mapper.toSearchParams(request);
        SellerPageResult pageResult = searchSellerByOffsetUseCase.execute(params);
        CustomPageableV1ApiResponse<SellerV1ApiResponse> response =
                mapper.toPageResponse(pageResult);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 셀러 상세 조회 API.
     *
     * <p>GET /api/v1/sellers/{sellerId}
     *
     * <p>셀러 전체 정보 (기본정보 + 주소 + 사업자정보 + CS + 계약 + 정산)를 조회합니다.
     *
     * @param sellerId 셀러 ID
     * @return 셀러 상세 정보
     */
    @Operation(summary = "셀러 상세 조회", description = "셀러 ID로 셀러 전체 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "셀러를 찾을 수 없음")
    })
    @GetMapping(SellerAdminV1Endpoints.SELLER_ID)
    public ResponseEntity<V1ApiResponse<SellerDetailV1ApiResponse>> fetchSeller(
            @Parameter(description = "셀러 ID", required = true)
                    @PathVariable(SellerAdminV1Endpoints.PATH_SELLER_ID)
                    Long sellerId) {
        SellerFullCompositeResult result = getSellerForAdminUseCase.execute(sellerId);
        SellerDetailV1ApiResponse response = mapper.toDetailResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 사업자등록번호 유효성 검증 API.
     *
     * <p>GET /api/v1/sellers/business-validation
     *
     * <p>사업자등록번호 중복 여부를 확인합니다.
     *
     * @param request 사업자등록번호 검증 요청
     * @return 사용 가능하면 true, 이미 등록된 번호면 false
     */
    @Operation(summary = "사업자등록번호 유효성 검증", description = "사업자등록번호의 중복 여부를 확인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "검증 완료")
    })
    @GetMapping(SellerAdminV1Endpoints.BUSINESS_VALIDATION)
    public ResponseEntity<V1ApiResponse<Boolean>> validateBusinessRegistration(
            @ParameterObject @Valid BusinessValidationV1ApiRequest request) {
        boolean isAvailable =
                validateBusinessRegistrationUseCase.execute(
                        request.registrationNumberWithoutHyphen());
        return ResponseEntity.ok(V1ApiResponse.success(isAvailable));
    }
}
