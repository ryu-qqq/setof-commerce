package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.ProductGroupImageAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.RegisterProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.UpdateProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.mapper.ProductGroupImageCommandApiMapper;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.port.in.command.RegisterProductGroupImagesUseCase;
import com.ryuqq.setof.application.productgroupimage.port.in.command.UpdateProductGroupImagesUseCase;
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
 * ProductGroupImageCommandController - 상품 그룹 이미지 등록/수정 API.
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
@Tag(name = "상품 그룹 이미지 관리", description = "상품 그룹 이미지 등록/수정 API")
@RestController
@RequestMapping(ProductGroupImageAdminEndpoints.BASE)
public class ProductGroupImageCommandController {

    private final RegisterProductGroupImagesUseCase registerUseCase;
    private final UpdateProductGroupImagesUseCase updateUseCase;
    private final ProductGroupImageCommandApiMapper mapper;

    /**
     * ProductGroupImageCommandController 생성자.
     *
     * @param registerUseCase 이미지 등록 UseCase
     * @param updateUseCase 이미지 수정 UseCase
     * @param mapper Command API 매퍼
     */
    public ProductGroupImageCommandController(
            RegisterProductGroupImagesUseCase registerUseCase,
            UpdateProductGroupImagesUseCase updateUseCase,
            ProductGroupImageCommandApiMapper mapper) {
        this.registerUseCase = registerUseCase;
        this.updateUseCase = updateUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품 그룹 이미지 등록 API.
     *
     * <p>상품 그룹의 이미지를 등록합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 등록 요청 DTO
     * @return 빈 응답 (201 Created)
     */
    @Operation(summary = "이미지 등록", description = "상품 그룹의 이미지를 등록합니다.")
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
    @PostMapping(ProductGroupImageAdminEndpoints.ID + ProductGroupImageAdminEndpoints.IMAGES)
    public ResponseEntity<ApiResponse<Void>> register(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductGroupImageAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Valid @RequestBody RegisterProductGroupImagesApiRequest request) {

        RegisterProductGroupImagesCommand command =
                mapper.toRegisterCommand(productGroupId, request);
        registerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of());
    }

    /**
     * 상품 그룹 이미지 수정 API.
     *
     * <p>상품 그룹의 이미지를 전체 교체합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 수정 요청 DTO
     * @return 빈 응답
     */
    @Operation(summary = "이미지 수정", description = "상품 그룹의 이미지를 전체 교체합니다.")
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
    @PutMapping(ProductGroupImageAdminEndpoints.ID + ProductGroupImageAdminEndpoints.IMAGES)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "상품 그룹 ID", required = true)
                    @PathVariable(ProductGroupImageAdminEndpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId,
            @Valid @RequestBody UpdateProductGroupImagesApiRequest request) {

        UpdateProductGroupImagesCommand command = mapper.toUpdateCommand(productGroupId, request);
        updateUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.of());
    }
}
