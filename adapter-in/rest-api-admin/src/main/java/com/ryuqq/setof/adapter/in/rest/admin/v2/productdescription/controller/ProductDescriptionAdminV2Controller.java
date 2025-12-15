package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.response.ProductDescriptionV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.mapper.ProductDescriptionAdminV2ApiMapper;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductDescription Admin V2 Controller
 *
 * <p>상품설명 관리 API 엔드포인트 (상품그룹 하위 리소스)
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductDescription", description = "상품설명 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductDescriptions.BASE)
@Validated
public class ProductDescriptionAdminV2Controller {

    private final GetProductDescriptionUseCase getProductDescriptionUseCase;
    private final UpdateProductDescriptionUseCase updateProductDescriptionUseCase;
    private final ProductDescriptionAdminV2ApiMapper mapper;

    public ProductDescriptionAdminV2Controller(
            GetProductDescriptionUseCase getProductDescriptionUseCase,
            UpdateProductDescriptionUseCase updateProductDescriptionUseCase,
            ProductDescriptionAdminV2ApiMapper mapper) {
        this.getProductDescriptionUseCase = getProductDescriptionUseCase;
        this.updateProductDescriptionUseCase = updateProductDescriptionUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품설명 조회", description = "상품그룹 ID로 상품설명을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품설명 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<ProductDescriptionV2ApiResponse>> getProductDescription(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId) {

        ProductDescriptionResponse response =
                getProductDescriptionUseCase
                        .findByProductGroupId(productGroupId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "상품설명을 찾을 수 없습니다. productGroupId="
                                                        + productGroupId));

        ProductDescriptionV2ApiResponse apiResponse =
                ProductDescriptionV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
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
