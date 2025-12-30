package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.BatchSetStockV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.SetStockV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.mapper.ProductStockAdminV2ApiMapper;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
 * ProductStock Admin Command Controller
 *
 * <p>재고 설정 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>재고 설정은 분산락 기반 동시성 제어 적용
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ProductStock", description = "재고 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ProductStocks.BASE)
@Validated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductStockAdminCommandController {

    private final SetStockUseCase setStockUseCase;
    private final ProductStockAdminV2ApiMapper mapper;

    public ProductStockAdminCommandController(
            SetStockUseCase setStockUseCase, ProductStockAdminV2ApiMapper mapper) {
        this.setStockUseCase = setStockUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "단일 상품 재고 설정", description = "상품의 재고 수량을 설정합니다. 분산락 기반 동시성 제어가 적용됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "재고 정보 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "423",
                        description = "락 획득 실패 (다른 요청 처리 중)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.ProductStocks.SINGLE_STOCK_PATH)
    public ResponseEntity<ApiResponse<Void>> setStock(
            @Parameter(description = "상품 ID") @PathVariable Long productId,
            @Valid @RequestBody SetStockV2ApiRequest request) {

        SetStockCommand command = mapper.toSetStockCommand(productId, request);
        setStockUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(
            summary = "상품 그룹 내 재고 일괄 설정",
            description = "상품 그룹에 속한 여러 상품의 재고를 일괄 설정합니다. 각 상품별로 분산락이 적용됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "423",
                        description = "일부 상품 락 획득 실패",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.ProductStocks.GROUP_STOCKS_PATH)
    public ResponseEntity<ApiResponse<Void>> setGroupStocks(
            @Parameter(description = "상품 그룹 ID") @PathVariable Long productGroupId,
            @Valid @RequestBody BatchSetStockV2ApiRequest request) {

        List<SetStockCommand> commands = mapper.toSetStockCommands(request);

        // 각 상품별로 순차적으로 재고 설정 (분산락은 UseCase 내부에서 처리)
        for (SetStockCommand command : commands) {
            setStockUseCase.execute(command);
        }

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
