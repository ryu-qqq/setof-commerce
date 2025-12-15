package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.response.ProductNoticeV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.mapper.ProductNoticeAdminV2ApiMapper;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import com.ryuqq.setof.application.productnotice.port.in.query.GetProductNoticeUseCase;
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
 * ProductNotice Admin V2 Controller
 *
 * <p>상품고시 관리 API 엔드포인트 (상품그룹 하위 리소스)
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductNotice", description = "상품고시 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductNotices.BASE)
@Validated
public class ProductNoticeAdminV2Controller {

    private final GetProductNoticeUseCase getProductNoticeUseCase;
    private final UpdateProductNoticeUseCase updateProductNoticeUseCase;
    private final ProductNoticeAdminV2ApiMapper mapper;

    public ProductNoticeAdminV2Controller(
            GetProductNoticeUseCase getProductNoticeUseCase,
            UpdateProductNoticeUseCase updateProductNoticeUseCase,
            ProductNoticeAdminV2ApiMapper mapper) {
        this.getProductNoticeUseCase = getProductNoticeUseCase;
        this.updateProductNoticeUseCase = updateProductNoticeUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "상품고시 조회", description = "상품그룹 ID로 상품고시를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품고시 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<ProductNoticeV2ApiResponse>> getProductNotice(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId) {

        ProductNoticeResponse response =
                getProductNoticeUseCase
                        .findByProductGroupId(productGroupId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "상품고시를 찾을 수 없습니다. productGroupId="
                                                        + productGroupId));

        ProductNoticeV2ApiResponse apiResponse = ProductNoticeV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "상품고시 수정", description = "상품그룹의 상품고시를 수정합니다.")
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
                        description = "상품고시 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateProductNotice(
            @Parameter(description = "상품그룹 ID") @PathVariable Long productGroupId,
            @Valid @RequestBody UpdateProductNoticeV2ApiRequest request) {

        UpdateProductNoticeCommand command = mapper.toUpdateCommand(request);
        updateProductNoticeUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
