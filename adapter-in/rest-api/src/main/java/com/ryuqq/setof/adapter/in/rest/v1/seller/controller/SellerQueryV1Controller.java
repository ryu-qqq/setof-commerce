package com.ryuqq.setof.adapter.in.rest.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.SellerV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.mapper.SellerV1ApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForCustomerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SellerQueryV1Controller - 셀러 조회 V1 Public API.
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
 * <p>레거시 SellerController.fetchSeller 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "셀러 조회 V1", description = "셀러 조회 V1 Public API")
@RestController
@RequestMapping(SellerV1Endpoints.SELLERS)
public class SellerQueryV1Controller {

    private final GetSellerForCustomerUseCase getSellerForCustomerUseCase;
    private final SellerV1ApiMapper mapper;

    public SellerQueryV1Controller(
            GetSellerForCustomerUseCase getSellerForCustomerUseCase, SellerV1ApiMapper mapper) {
        this.getSellerForCustomerUseCase = getSellerForCustomerUseCase;
        this.mapper = mapper;
    }

    /**
     * 셀러 단건 조회 API (고객용).
     *
     * <p>GET /api/v1/seller/{sellerId}
     *
     * <p>셀러 기본 정보 + CS 정보 + 사업자 정보를 조회합니다.
     *
     * @param sellerId 셀러 ID
     * @return 셀러 상세 정보 (고객용)
     */
    @Operation(summary = "셀러 단건 조회", description = "셀러 ID로 셀러 정보를 조회합니다 (고객용).")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "셀러를 찾을 수 없음 (레거시 호환: HTTP 200, body status 404)")
    })
    @GetMapping(SellerV1Endpoints.SELLER_ID)
    public ResponseEntity<V1ApiResponse<SellerV1ApiResponse>> getSeller(
            @Parameter(description = "셀러 ID", required = true)
                    @PathVariable(SellerV1Endpoints.PATH_SELLER_ID)
                    Long sellerId) {
        SellerCompositeResult result = getSellerForCustomerUseCase.execute(sellerId);
        SellerV1ApiResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
