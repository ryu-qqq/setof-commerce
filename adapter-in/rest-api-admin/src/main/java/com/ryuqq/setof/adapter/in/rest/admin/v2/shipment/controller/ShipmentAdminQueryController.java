package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.response.ShipmentV2ApiResponse;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentsByCheckoutUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentsBySellerUseCase;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
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
 * Shipment Admin Query Controller
 *
 * <p>운송장 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin Shipment", description = "운송장 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Shipments.BASE)
@Validated
public class ShipmentAdminQueryController {

    private final GetShipmentUseCase getShipmentUseCase;
    private final GetShipmentsBySellerUseCase getShipmentsBySellerUseCase;
    private final GetShipmentsByCheckoutUseCase getShipmentsByCheckoutUseCase;

    public ShipmentAdminQueryController(
            GetShipmentUseCase getShipmentUseCase,
            GetShipmentsBySellerUseCase getShipmentsBySellerUseCase,
            GetShipmentsByCheckoutUseCase getShipmentsByCheckoutUseCase) {
        this.getShipmentUseCase = getShipmentUseCase;
        this.getShipmentsBySellerUseCase = getShipmentsBySellerUseCase;
        this.getShipmentsByCheckoutUseCase = getShipmentsByCheckoutUseCase;
    }

    @Operation(summary = "셀러별 운송장 목록 조회", description = "셀러 ID로 운송장 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShipmentV2ApiResponse>>> getShipmentsBySeller(
            @Parameter(description = "셀러 ID", required = true) @RequestParam Long sellerId) {

        List<Shipment> shipments = getShipmentsBySellerUseCase.getBySellerId(sellerId);

        List<ShipmentV2ApiResponse> apiResponses =
                shipments.stream().map(ShipmentV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "운송장 상세 조회", description = "운송장 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "운송장 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Shipments.ID_PATH)
    public ResponseEntity<ApiResponse<ShipmentV2ApiResponse>> getShipment(
            @Parameter(description = "운송장 ID") @PathVariable Long shipmentId) {

        Shipment shipment = getShipmentUseCase.execute(shipmentId);
        ShipmentV2ApiResponse apiResponse = ShipmentV2ApiResponse.from(shipment);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "결제건별 운송장 목록 조회", description = "결제건 ID로 운송장 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Shipments.BY_CHECKOUT_PATH)
    public ResponseEntity<ApiResponse<List<ShipmentV2ApiResponse>>> getShipmentsByCheckout(
            @Parameter(description = "결제건 ID") @PathVariable Long checkoutId) {

        List<Shipment> shipments = getShipmentsByCheckoutUseCase.getByCheckoutId(checkoutId);

        List<ShipmentV2ApiResponse> apiResponses =
                shipments.stream().map(ShipmentV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }
}
