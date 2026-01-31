package com.ryuqq.setof.adapter.in.rest.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.SellerV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerInfoV1ApiResponse;
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

    private final GetSellerForCustomerUseCase getSellerForCustomerUseCase;
    private final SellerV1ApiMapper mapper;

    public SellerQueryV1Controller(
            GetSellerForCustomerUseCase getSellerForCustomerUseCase, SellerV1ApiMapper mapper) {
        this.getSellerForCustomerUseCase = getSellerForCustomerUseCase;
        this.mapper = mapper;
    }

    /**
     * 특정 셀러 조회 API.
     *
     * <p>셀러 ID로 셀러 정보를 조회합니다.
     *
     * @param sellerId 셀러 ID
     * @return 셀러 정보
     */
    @Operation(summary = "셀러 조회", description = "셀러 ID로 셀러 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "셀러를 찾을 수 없음")
    })
    @GetMapping(SellerV1Endpoints.SELLER_BY_ID)
    public ResponseEntity<V1ApiResponse<SellerInfoV1ApiResponse>> getSeller(
            @Parameter(description = "셀러 ID", required = true)
                    @PathVariable(SellerV1Endpoints.PATH_SELLER_ID)
                    Long sellerId) {
        SellerCompositeResult result = getSellerForCustomerUseCase.execute(sellerId);
        SellerInfoV1ApiResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
