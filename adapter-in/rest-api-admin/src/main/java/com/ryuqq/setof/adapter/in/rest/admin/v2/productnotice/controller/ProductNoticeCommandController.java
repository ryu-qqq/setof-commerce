package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.ProductNoticeAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.RegisterProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.mapper.ProductNoticeCommandApiMapper;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.port.in.command.RegisterProductNoticeUseCase;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductNoticeCommandController - 상품 그룹 고시정보 등록/수정 API.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
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
@Tag(name = "상품 그룹 고시정보 관리", description = "상품 그룹 고시정보 등록/수정 API")
@RestController
@RequestMapping(ProductNoticeAdminEndpoints.BASE)
public class ProductNoticeCommandController {

    private final RegisterProductNoticeUseCase registerUseCase;
    private final UpdateProductNoticeUseCase updateUseCase;
    private final ProductNoticeCommandApiMapper mapper;

    /**
     * ProductNoticeCommandController 생성자.
     *
     * @param registerUseCase 고시정보 등록 UseCase
     * @param updateUseCase 고시정보 수정 UseCase
     * @param mapper Command API 매퍼
     */
    public ProductNoticeCommandController(
            RegisterProductNoticeUseCase registerUseCase,
            UpdateProductNoticeUseCase updateUseCase,
            ProductNoticeCommandApiMapper mapper) {
        this.registerUseCase = registerUseCase;
        this.updateUseCase = updateUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품 그룹 고시정보 등록 API.
     *
     * <p>상품 그룹의 고시정보를 등록합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 등록 요청 DTO
     * @return 생성된 고시정보 ID
     */
    @Operation(summary = "고시정보 등록", description = "상품 그룹의 고시정보를 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "상품 그룹을 찾을 수 없음")
    })
    @PostMapping(ProductNoticeAdminEndpoints.ID + ProductNoticeAdminEndpoints.NOTICE)
    public ResponseEntity<ApiResponse<Long>> register(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductNoticeAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Valid @RequestBody RegisterProductNoticeApiRequest request) {

        RegisterProductNoticeCommand command = mapper.toRegisterCommand(productGroupId, request);
        Long createdId = registerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(createdId));
    }

    /**
     * 상품 그룹 고시정보 수정 API.
     *
     * <p>상품 그룹의 고시정보를 수정합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 수정 요청 DTO
     * @return 빈 응답
     */
    @Operation(summary = "고시정보 수정", description = "상품 그룹의 고시정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "상품 그룹을 찾을 수 없음")
    })
    @PutMapping(ProductNoticeAdminEndpoints.ID + ProductNoticeAdminEndpoints.NOTICE)
    public ResponseEntity<Void> update(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductNoticeAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Valid @RequestBody UpdateProductNoticeApiRequest request) {

        UpdateProductNoticeCommand command = mapper.toUpdateCommand(productGroupId, request);
        updateUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }
}
