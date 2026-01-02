package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ApproveClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.CompleteClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmExchangeDeliveredV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ConfirmReturnReceivedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterExchangeShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RegisterReturnShippingV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.RejectClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.ScheduleReturnPickupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request.UpdateReturnShippingStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.response.ClaimAdminV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.mapper.ClaimAdminV2ApiMapper;
import com.ryuqq.setof.application.claim.dto.command.ApproveClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.CompleteClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.ConfirmExchangeDeliveredCommand;
import com.ryuqq.setof.application.claim.dto.command.ConfirmReturnReceivedCommand;
import com.ryuqq.setof.application.claim.dto.command.RegisterExchangeShippingCommand;
import com.ryuqq.setof.application.claim.dto.command.RegisterReturnShippingCommand;
import com.ryuqq.setof.application.claim.dto.command.RejectClaimCommand;
import com.ryuqq.setof.application.claim.dto.command.ScheduleReturnPickupCommand;
import com.ryuqq.setof.application.claim.dto.command.UpdateReturnShippingStatusCommand;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.command.ApproveClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.command.CompleteClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.command.ConfirmExchangeDeliveredUseCase;
import com.ryuqq.setof.application.claim.port.in.command.ConfirmReturnReceivedUseCase;
import com.ryuqq.setof.application.claim.port.in.command.RegisterExchangeShippingUseCase;
import com.ryuqq.setof.application.claim.port.in.command.RegisterReturnShippingUseCase;
import com.ryuqq.setof.application.claim.port.in.command.RejectClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.command.ScheduleReturnPickupUseCase;
import com.ryuqq.setof.application.claim.port.in.command.UpdateReturnShippingStatusUseCase;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimUseCase;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Claim Admin Command Controller
 *
 * <p>클레임 상태 변경 및 처리 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>클레임 상태 흐름:
 *
 * <pre>
 * REQUESTED → APPROVED → IN_PROGRESS → COMPLETED
 *    ↓           ↓
 * CANCELLED  REJECTED
 * </pre>
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PATCH 메서드만 포함
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin - Claim", description = "클레임 관리 API (관리자)")
@RestController
@RequestMapping(ApiV2Paths.Claims.BASE)
@Validated
public class ClaimAdminCommandController {

    private final ApproveClaimUseCase approveClaimUseCase;
    private final RejectClaimUseCase rejectClaimUseCase;
    private final CompleteClaimUseCase completeClaimUseCase;
    private final GetClaimUseCase getClaimUseCase;
    private final ScheduleReturnPickupUseCase scheduleReturnPickupUseCase;
    private final RegisterReturnShippingUseCase registerReturnShippingUseCase;
    private final UpdateReturnShippingStatusUseCase updateReturnShippingStatusUseCase;
    private final ConfirmReturnReceivedUseCase confirmReturnReceivedUseCase;
    private final RegisterExchangeShippingUseCase registerExchangeShippingUseCase;
    private final ConfirmExchangeDeliveredUseCase confirmExchangeDeliveredUseCase;
    private final ClaimAdminV2ApiMapper claimAdminV2ApiMapper;

    public ClaimAdminCommandController(
            ApproveClaimUseCase approveClaimUseCase,
            RejectClaimUseCase rejectClaimUseCase,
            CompleteClaimUseCase completeClaimUseCase,
            GetClaimUseCase getClaimUseCase,
            ScheduleReturnPickupUseCase scheduleReturnPickupUseCase,
            RegisterReturnShippingUseCase registerReturnShippingUseCase,
            UpdateReturnShippingStatusUseCase updateReturnShippingStatusUseCase,
            ConfirmReturnReceivedUseCase confirmReturnReceivedUseCase,
            RegisterExchangeShippingUseCase registerExchangeShippingUseCase,
            ConfirmExchangeDeliveredUseCase confirmExchangeDeliveredUseCase,
            ClaimAdminV2ApiMapper claimAdminV2ApiMapper) {
        this.approveClaimUseCase = approveClaimUseCase;
        this.rejectClaimUseCase = rejectClaimUseCase;
        this.completeClaimUseCase = completeClaimUseCase;
        this.getClaimUseCase = getClaimUseCase;
        this.scheduleReturnPickupUseCase = scheduleReturnPickupUseCase;
        this.registerReturnShippingUseCase = registerReturnShippingUseCase;
        this.updateReturnShippingStatusUseCase = updateReturnShippingStatusUseCase;
        this.confirmReturnReceivedUseCase = confirmReturnReceivedUseCase;
        this.registerExchangeShippingUseCase = registerExchangeShippingUseCase;
        this.confirmExchangeDeliveredUseCase = confirmExchangeDeliveredUseCase;
        this.claimAdminV2ApiMapper = claimAdminV2ApiMapper;
    }

    /**
     * 클레임 승인
     *
     * <p>고객의 클레임 요청을 승인합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 승인 요청 정보
     * @return 승인된 클레임 정보
     */
    @Operation(summary = "클레임 승인", description = "고객의 클레임 요청을 승인합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "승인 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "승인 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.APPROVE_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> approveClaim(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody ApproveClaimV2ApiRequest request) {

        ApproveClaimCommand command = claimAdminV2ApiMapper.toApproveCommand(claimId, request);
        approveClaimUseCase.approve(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 클레임 반려
     *
     * <p>고객의 클레임 요청을 반려합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 반려 요청 정보
     * @return 반려된 클레임 정보
     */
    @Operation(summary = "클레임 반려", description = "고객의 클레임 요청을 반려합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "반려 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "반려 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.REJECT_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> rejectClaim(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody RejectClaimV2ApiRequest request) {

        RejectClaimCommand command = claimAdminV2ApiMapper.toRejectCommand(claimId, request);
        rejectClaimUseCase.reject(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 클레임 완료 처리
     *
     * <p>클레임 처리를 완료합니다. (환불 완료, 교환품 발송 완료 등)
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 완료 요청 정보
     * @return 완료된 클레임 정보
     */
    @Operation(summary = "클레임 완료 처리", description = "클레임 처리를 완료합니다. (환불 완료, 교환품 발송 완료 등)")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "완료 처리 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "완료 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.COMPLETE_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> completeClaim(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody CompleteClaimV2ApiRequest request) {

        CompleteClaimCommand command = claimAdminV2ApiMapper.toCompleteCommand(claimId, request);
        completeClaimUseCase.complete(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    // ========== 반품 배송 관리 API ==========

    /**
     * 방문수거 예약
     *
     * <p>클레임 승인 후 택배사에 방문수거를 예약합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 수거 예약 요청 정보
     * @return 업데이트된 클레임 정보
     */
    @Operation(summary = "방문수거 예약", description = "클레임 승인 후 택배사에 방문수거를 예약합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "예약 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "수거 예약 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.RETURN_PICKUP_SCHEDULE_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> scheduleReturnPickup(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody ScheduleReturnPickupV2ApiRequest request) {

        ScheduleReturnPickupCommand command =
                claimAdminV2ApiMapper.toScheduleReturnPickupCommand(claimId, request);
        scheduleReturnPickupUseCase.schedulePickup(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 반품 배송 등록 (송장 번호 등록)
     *
     * <p>고객이 직접 발송하거나, 착불 송장 발급 시 사용합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 반품 배송 등록 요청
     * @return 업데이트된 클레임 정보
     */
    @Operation(summary = "반품 배송 등록", description = "고객이 직접 발송하거나, 착불 송장 발급 시 사용합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "배송 등록 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.RETURN_SHIPPING_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> registerReturnShipping(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody RegisterReturnShippingV2ApiRequest request) {

        RegisterReturnShippingCommand command =
                claimAdminV2ApiMapper.toRegisterReturnShippingCommand(claimId, request);
        registerReturnShippingUseCase.registerShipping(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 반품 배송 상태 업데이트
     *
     * <p>택배사 Webhook이나 관리자에 의해 배송 상태를 변경합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 배송 상태 업데이트 요청
     * @return 업데이트된 클레임 정보
     */
    @Operation(summary = "반품 배송 상태 업데이트", description = "택배사 Webhook이나 관리자에 의해 배송 상태를 변경합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "상태 업데이트 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.RETURN_SHIPPING_STATUS_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> updateReturnShippingStatus(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody UpdateReturnShippingStatusV2ApiRequest request) {

        UpdateReturnShippingStatusCommand command =
                claimAdminV2ApiMapper.toUpdateReturnShippingStatusCommand(claimId, request);
        updateReturnShippingStatusUseCase.updateStatus(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 반품 수령 확인 및 검수 결과 등록
     *
     * <p>판매자가 반품 상품을 수령하고 검수 결과를 등록합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 수령 확인 및 검수 결과 요청
     * @return 업데이트된 클레임 정보
     */
    @Operation(summary = "반품 수령 확인", description = "판매자가 반품 상품을 수령하고 검수 결과를 등록합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수령 확인 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "수령 확인 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.RETURN_RECEIVED_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> confirmReturnReceived(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody ConfirmReturnReceivedV2ApiRequest request) {

        ConfirmReturnReceivedCommand command =
                claimAdminV2ApiMapper.toConfirmReturnReceivedCommand(claimId, request);
        confirmReturnReceivedUseCase.confirmReceived(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    // ========== 교환 배송 관리 API ==========

    /**
     * 교환품 발송 등록
     *
     * <p>교환 클레임에서 새 상품(교환품)을 발송할 때 사용합니다. 반품 수령 완료 및 검수 통과 후 발송 가능합니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 교환품 발송 요청
     * @return 업데이트된 클레임 정보
     */
    @Operation(
            summary = "교환품 발송 등록",
            description = "교환 클레임에서 새 상품(교환품)을 발송합니다. 반품 수령 완료 및 검수 통과 후 발송 가능합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "발송 등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "발송 불가 상태 (교환 클레임 아님, 반품 미수령, 검수 불합격)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.EXCHANGE_SHIPPING_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> registerExchangeShipping(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody RegisterExchangeShippingV2ApiRequest request) {

        RegisterExchangeShippingCommand command =
                claimAdminV2ApiMapper.toRegisterExchangeShippingCommand(claimId, request);
        registerExchangeShippingUseCase.registerShipping(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 교환품 수령 확인
     *
     * <p>고객이 교환품을 수령했음을 확인합니다. 확인 시 클레임이 자동으로 완료 처리됩니다.
     *
     * @param claimId 클레임 ID (UUID)
     * @param request 수령 확인 요청 (빈 body)
     * @return 완료된 클레임 정보
     */
    @Operation(
            summary = "교환품 수령 확인",
            description = "고객이 교환품을 수령했음을 확인합니다. 확인 시 클레임이 자동으로 완료 처리됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수령 확인 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "수령 확인 불가 상태 (교환품 미발송)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.ID_PATH + ApiV2Paths.Claims.EXCHANGE_DELIVERED_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> confirmExchangeDelivered(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId,
            @Valid @RequestBody ConfirmExchangeDeliveredV2ApiRequest request) {

        ConfirmExchangeDeliveredCommand command =
                claimAdminV2ApiMapper.toConfirmExchangeDeliveredCommand(claimId, request);
        confirmExchangeDeliveredUseCase.confirmDelivered(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
