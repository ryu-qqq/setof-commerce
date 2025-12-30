package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerAdminV2ApiMapper;
import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Seller Admin Command Controller
 *
 * <p>셀러 등록/수정/상태변경 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin Seller", description = "셀러 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Sellers.BASE)
@Validated
public class SellerAdminCommandController {

    private final RegisterSellerUseCase registerSellerUseCase;
    private final UpdateSellerUseCase updateSellerUseCase;
    private final UpdateApprovalStatusUseCase updateApprovalStatusUseCase;
    private final DeleteSellerUseCase deleteSellerUseCase;
    private final SellerAdminV2ApiMapper mapper;

    public SellerAdminCommandController(
            RegisterSellerUseCase registerSellerUseCase,
            UpdateSellerUseCase updateSellerUseCase,
            UpdateApprovalStatusUseCase updateApprovalStatusUseCase,
            DeleteSellerUseCase deleteSellerUseCase,
            SellerAdminV2ApiMapper mapper) {
        this.registerSellerUseCase = registerSellerUseCase;
        this.updateSellerUseCase = updateSellerUseCase;
        this.updateApprovalStatusUseCase = updateApprovalStatusUseCase;
        this.deleteSellerUseCase = deleteSellerUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "셀러 등록", description = "새로운 셀러를 등록합니다. 초기 상태는 PENDING입니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 등록된 사업자등록번호",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerSeller(
            @Valid @RequestBody RegisterSellerV2ApiRequest request) {

        RegisterSellerCommand command = mapper.toRegisterCommand(request);
        Long sellerId = registerSellerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(sellerId));
    }

    @Operation(summary = "셀러 수정", description = "셀러 정보를 수정합니다.")
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
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@sellerAccess.canModify(#sellerId)")
    @PutMapping(ApiV2Paths.Sellers.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Valid @RequestBody UpdateSellerV2ApiRequest request) {

        UpdateSellerCommand command = mapper.toUpdateCommand(sellerId, request);
        updateSellerUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "셀러 승인", description = "대기 상태의 셀러를 승인합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "승인 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "승인 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.tenantAdminOrHigher() and @sellerAccess.canAccess(#sellerId)")
    @PatchMapping(ApiV2Paths.Sellers.APPROVE_PATH)
    public ResponseEntity<ApiResponse<Void>> approveSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        UpdateApprovalStatusCommand command = mapper.toApproveCommand(sellerId);
        updateApprovalStatusUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "셀러 거절", description = "대기 상태의 셀러를 거절합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "거절 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "거절 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.tenantAdminOrHigher() and @sellerAccess.canAccess(#sellerId)")
    @PatchMapping(ApiV2Paths.Sellers.REJECT_PATH)
    public ResponseEntity<ApiResponse<Void>> rejectSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        UpdateApprovalStatusCommand command = mapper.toRejectCommand(sellerId);
        updateApprovalStatusUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "셀러 정지", description = "승인된 셀러를 정지합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "정지 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "정지 불가 상태",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.tenantAdminOrHigher() and @sellerAccess.canAccess(#sellerId)")
    @PatchMapping(ApiV2Paths.Sellers.SUSPEND_PATH)
    public ResponseEntity<ApiResponse<Void>> suspendSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        UpdateApprovalStatusCommand command = mapper.toSuspendCommand(sellerId);
        updateApprovalStatusUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "셀러 삭제", description = "셀러를 삭제합니다 (소프트 삭제).")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@sellerAccess.canDelete(#sellerId)")
    @PatchMapping(ApiV2Paths.Sellers.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        DeleteSellerCommand command = mapper.toDeleteCommand(sellerId);
        deleteSellerUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
