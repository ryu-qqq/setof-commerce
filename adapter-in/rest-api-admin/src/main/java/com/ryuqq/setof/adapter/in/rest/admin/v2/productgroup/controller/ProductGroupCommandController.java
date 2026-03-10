package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.ProductGroupAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupBasicInfoApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupFullApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupIdApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper.ProductGroupCommandApiMapper;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.port.in.command.RegisterProductGroupFullUseCase;
import com.ryuqq.setof.application.productgroup.port.in.command.UpdateProductGroupBasicInfoUseCase;
import com.ryuqq.setof.application.productgroup.port.in.command.UpdateProductGroupFullUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductGroupCommandController - 상품 그룹 생성/수정 API.
 *
 * <p>상품 그룹 등록/수정 엔드포인트를 제공합니다. 서버 내부 호출 전용으로 인증/인가를 적용하지 않습니다.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-002: DELETE 메서드 금지 (소프트 삭제는 PATCH).
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-009: @Valid 어노테이션 필수.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "상품 그룹 관리", description = "상품 그룹 등록/수정 API")
@RestController
@RequestMapping(ProductGroupAdminEndpoints.PRODUCT_GROUPS)
public class ProductGroupCommandController {

    private final RegisterProductGroupFullUseCase registerUseCase;
    private final UpdateProductGroupFullUseCase updateUseCase;
    private final UpdateProductGroupBasicInfoUseCase updateBasicInfoUseCase;
    private final ProductGroupCommandApiMapper mapper;

    /**
     * ProductGroupCommandController 생성자.
     *
     * @param registerUseCase 상품 그룹 등록 UseCase
     * @param updateUseCase 상품 그룹 전체 수정 UseCase
     * @param updateBasicInfoUseCase 기본 정보 수정 UseCase
     * @param mapper Command API 매퍼
     */
    public ProductGroupCommandController(
            RegisterProductGroupFullUseCase registerUseCase,
            UpdateProductGroupFullUseCase updateUseCase,
            UpdateProductGroupBasicInfoUseCase updateBasicInfoUseCase,
            ProductGroupCommandApiMapper mapper) {
        this.registerUseCase = registerUseCase;
        this.updateUseCase = updateUseCase;
        this.updateBasicInfoUseCase = updateBasicInfoUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품 그룹 등록 API.
     *
     * <p>ProductGroup + Description + Notice + Products를 한번에 등록합니다.
     *
     * @param request 등록 요청 DTO
     * @return 생성된 상품 그룹 ID
     */
    @Operation(summary = "상품 그룹 등록", description = "상품 그룹과 하위 상품들을 한번에 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductGroupIdApiResponse>> registerProductGroup(
            @Valid @RequestBody RegisterProductGroupApiRequest request) {

        RegisterProductGroupCommand command = mapper.toCommand(request);
        Long productGroupId = registerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ProductGroupIdApiResponse.of(productGroupId)));
    }

    /**
     * 상품 그룹 전체 수정 API.
     *
     * <p>ProductGroup + Description + Notice + Products를 한번에 수정합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 수정 요청 DTO
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "상품 그룹 전체 수정", description = "상품 그룹과 하위 상품들을 한번에 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "상품 그룹을 찾을 수 없음")
    })
    @PutMapping(ProductGroupAdminEndpoints.ID)
    public ResponseEntity<Void> updateProductGroupFull(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductGroupAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Valid @RequestBody UpdateProductGroupFullApiRequest request) {

        UpdateProductGroupFullCommand command = mapper.toCommand(productGroupId, request);
        updateUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 그룹 기본 정보 수정 API.
     *
     * <p>상품명, 브랜드, 카테고리, 배송정책, 반품정책을 수정합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 기본 정보 수정 요청 DTO
     * @return 빈 응답 (204 No Content)
     */
    @Operation(summary = "기본 정보 수정", description = "상품 그룹의 기본 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "상품 그룹을 찾을 수 없음")
    })
    @PatchMapping(ProductGroupAdminEndpoints.ID + ProductGroupAdminEndpoints.BASIC_INFO)
    public ResponseEntity<Void> updateBasicInfo(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductGroupAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Valid @RequestBody UpdateProductGroupBasicInfoApiRequest request) {

        UpdateProductGroupBasicInfoCommand command = mapper.toCommand(productGroupId, request);
        updateBasicInfoUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }
}
