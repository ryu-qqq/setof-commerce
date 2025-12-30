package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper.ProductGroupAdminV2ApiMapper;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductGroup Admin Command Controller
 *
 * <p>상품그룹 등록/수정 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH, DELETE 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductGroup", description = "상품그룹 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductGroups.BASE)
@Validated
public class ProductGroupAdminCommandController {

    private final RegisterFullProductUseCase registerFullProductUseCase;
    private final UpdateFullProductUseCase updateFullProductUseCase;
    private final UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;
    private final ProductGroupAdminV2ApiMapper mapper;

    public ProductGroupAdminCommandController(
            RegisterFullProductUseCase registerFullProductUseCase,
            UpdateFullProductUseCase updateFullProductUseCase,
            UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase,
            ProductGroupAdminV2ApiMapper mapper) {
        this.registerFullProductUseCase = registerFullProductUseCase;
        this.updateFullProductUseCase = updateFullProductUseCase;
        this.updateProductGroupStatusUseCase = updateProductGroupStatusUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품그룹 등록 (Full)", description = "상품그룹 + SKU + 이미지 + 설명 + 고시를 일괄 등록합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerProductGroup(
            @Valid @RequestBody RegisterProductGroupV2ApiRequest request) {

        RegisterFullProductCommand command = mapper.toRegisterCommand(request);
        Long productGroupId = registerFullProductUseCase.registerFullProduct(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(productGroupId));
    }

    @Operation(
            summary = "상품그룹 수정 (Full)",
            description = "상품그룹 + SKU + 이미지 + 설명 + 고시를 일괄 수정합니다. Diff 비교 후 변경분만 업데이트합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품그룹 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PutMapping(ApiV2Paths.ProductGroups.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateProductGroup(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Valid @RequestBody UpdateProductGroupV2ApiRequest request) {

        UpdateFullProductCommand command = mapper.toUpdateCommand(productGroupId, request);
        updateFullProductUseCase.updateFullProduct(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "상품그룹 상태 변경", description = "상품그룹의 상태를 변경합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "상태 변경 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 상태 전이",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품그룹 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@sellerAccess.canAccess(#sellerId)")
    @PatchMapping(ApiV2Paths.ProductGroups.STATUS_PATH)
    public ResponseEntity<ApiResponse<Void>> updateProductGroupStatus(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Parameter(description = "셀러 ID (권한 검증용)") @RequestParam Long sellerId,
            @Valid @RequestBody UpdateProductGroupStatusV2ApiRequest request) {

        UpdateProductGroupStatusCommand command =
                mapper.toStatusCommand(productGroupId, sellerId, request);
        updateProductGroupStatusUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
