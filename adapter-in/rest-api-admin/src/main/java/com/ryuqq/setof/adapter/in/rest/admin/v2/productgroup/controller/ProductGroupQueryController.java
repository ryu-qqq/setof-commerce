package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.ProductGroupAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper.ProductGroupQueryApiMapper;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.port.in.query.GetAdminProductGroupUseCase;
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
 * ProductGroupQueryController - Admin 상품 그룹 조회 API.
 *
 * <p>Admin 전용 상품 그룹 상세 조회 엔드포인트를 제공합니다. Service Token 인증을 통해 접근 권한을 검증합니다.
 *
 * <p>API-CTR-001: @RestController 어노테이션 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>API-CTR-010: CQRS Controller 분리 (Query 전용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "상품 그룹 조회", description = "Admin 상품 그룹 조회 API")
@RestController
@RequestMapping(ProductGroupAdminEndpoints.PRODUCT_GROUPS)
public class ProductGroupQueryController {

    private final GetAdminProductGroupUseCase getAdminProductGroupUseCase;
    private final ProductGroupQueryApiMapper mapper;

    public ProductGroupQueryController(
            GetAdminProductGroupUseCase getAdminProductGroupUseCase,
            ProductGroupQueryApiMapper mapper) {
        this.getAdminProductGroupUseCase = getAdminProductGroupUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품 그룹 상세 조회 API.
     *
     * <p>상품 그룹 ID로 상세 정보를 조회합니다. 기본 정보 + 개별 상품 목록 + 이미지 목록을 포함합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 상품 그룹 상세 응답
     */
    @Operation(summary = "상품 그룹 상세 조회", description = "상품 그룹 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "상품 그룹을 찾을 수 없음")
    })
    @GetMapping(ProductGroupAdminEndpoints.ID)
    public ResponseEntity<ApiResponse<ProductGroupDetailApiResponse>> getById(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductGroupAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId) {

        ProductGroupDetailCompositeResult result =
                getAdminProductGroupUseCase.execute(productGroupId);
        ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
