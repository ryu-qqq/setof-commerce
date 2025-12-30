package com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.response.CarrierV2ApiResponse;
import com.ryuqq.setof.application.carrier.port.in.query.GetCarrierUseCase;
import com.ryuqq.setof.application.carrier.port.in.query.GetCarriersUseCase;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Carrier Admin Query Controller
 *
 * <p>택배사 조회 API 엔드포인트 (CQRS Query 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>GET 메서드만 포함
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin Carrier", description = "택배사 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Carriers.BASE)
@Validated
public class CarrierAdminQueryController {

    private final GetCarrierUseCase getCarrierUseCase;
    private final GetCarriersUseCase getCarriersUseCase;

    public CarrierAdminQueryController(
            GetCarrierUseCase getCarrierUseCase, GetCarriersUseCase getCarriersUseCase) {
        this.getCarrierUseCase = getCarrierUseCase;
        this.getCarriersUseCase = getCarriersUseCase;
    }

    @Operation(summary = "택배사 목록 조회", description = "택배사 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarrierV2ApiResponse>>> getCarriers(
            @Parameter(description = "활성 택배사만 조회 여부 (기본값: false)")
                    @RequestParam(defaultValue = "false")
                    boolean activeOnly) {

        List<Carrier> carriers =
                activeOnly
                        ? getCarriersUseCase.getActiveCarriers()
                        : getCarriersUseCase.getAllCarriers();

        List<CarrierV2ApiResponse> apiResponses =
                carriers.stream().map(CarrierV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "택배사 상세 조회", description = "택배사 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "택배사 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Carriers.ID_PATH)
    public ResponseEntity<ApiResponse<CarrierV2ApiResponse>> getCarrier(
            @Parameter(description = "택배사 ID") @PathVariable Long carrierId) {

        Carrier carrier = getCarrierUseCase.execute(carrierId);
        CarrierV2ApiResponse apiResponse = CarrierV2ApiResponse.from(carrier);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
