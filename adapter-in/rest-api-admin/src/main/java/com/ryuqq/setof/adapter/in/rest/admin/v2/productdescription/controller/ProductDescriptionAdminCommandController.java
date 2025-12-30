package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.mapper.ProductDescriptionAdminV2ApiMapper;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductDescription Admin Command Controller
 *
 * <p>상품설명 수정 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductDescription", description = "상품설명 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductDescriptions.BASE)
@Validated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductDescriptionAdminCommandController {

    private final UpdateProductDescriptionUseCase updateProductDescriptionUseCase;
    private final ProductDescriptionAdminV2ApiMapper mapper;

    public ProductDescriptionAdminCommandController(
            UpdateProductDescriptionUseCase updateProductDescriptionUseCase,
            ProductDescriptionAdminV2ApiMapper mapper) {
        this.updateProductDescriptionUseCase = updateProductDescriptionUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품설명 수정", description = "상품그룹의 상품설명을 수정합니다.")
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
                        description = "상품설명 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateProductDescription(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Valid @RequestBody UpdateProductDescriptionV2ApiRequest request) {

        UpdateProductDescriptionCommand command = mapper.toUpdateCommand(request);
        updateProductDescriptionUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
