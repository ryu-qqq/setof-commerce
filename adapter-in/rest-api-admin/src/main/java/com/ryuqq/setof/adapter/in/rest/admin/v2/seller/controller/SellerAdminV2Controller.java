package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SellerSearchV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerPageV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerAdminV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Seller Admin V2 Controller
 *
 * <p>셀러 관리 API 엔드포인트 (등록/조회/수정/승인/거절/정지/삭제)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>Command/Query 분리 (CQRS)
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin Seller V2", description = "셀러 관리 API V2")
@RestController
@RequestMapping("/api/v2/admin/sellers")
@Validated
public class SellerAdminV2Controller {

    private final RegisterSellerUseCase registerSellerUseCase;
    private final GetSellerUseCase getSellerUseCase;
    private final GetSellersUseCase getSellersUseCase;
    private final UpdateSellerUseCase updateSellerUseCase;
    private final UpdateApprovalStatusUseCase updateApprovalStatusUseCase;
    private final DeleteSellerUseCase deleteSellerUseCase;
    private final SellerAdminV2ApiMapper mapper;

    public SellerAdminV2Controller(
            RegisterSellerUseCase registerSellerUseCase,
            GetSellerUseCase getSellerUseCase,
            GetSellersUseCase getSellersUseCase,
            UpdateSellerUseCase updateSellerUseCase,
            UpdateApprovalStatusUseCase updateApprovalStatusUseCase,
            DeleteSellerUseCase deleteSellerUseCase,
            SellerAdminV2ApiMapper mapper) {
        this.registerSellerUseCase = registerSellerUseCase;
        this.getSellerUseCase = getSellerUseCase;
        this.getSellersUseCase = getSellersUseCase;
        this.updateSellerUseCase = updateSellerUseCase;
        this.updateApprovalStatusUseCase = updateApprovalStatusUseCase;
        this.deleteSellerUseCase = deleteSellerUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "셀러 목록 조회", description = "검색 조건에 맞는 셀러 목록을 페이징하여 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<SellerPageV2ApiResponse>> searchSellers(
            @Valid @ModelAttribute SellerSearchV2ApiRequest request) {

        SellerSearchQuery query =
                mapper.toSearchQuery(
                        request.sellerName(),
                        request.approvalStatus(),
                        request.page(),
                        request.size());

        PageResponse<SellerSummaryResponse> pageResponse = getSellersUseCase.execute(query);

        List<SellerSummaryV2ApiResponse> apiResponses =
                pageResponse.content().stream().map(SellerSummaryV2ApiResponse::from).toList();

        SellerPageV2ApiResponse response =
                SellerPageV2ApiResponse.of(
                        apiResponses,
                        pageResponse.page(),
                        pageResponse.size(),
                        pageResponse.totalElements());

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    @Operation(summary = "셀러 상세 조회", description = "셀러 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "셀러 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<SellerV2ApiResponse>> getSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        SellerResponse response = getSellerUseCase.execute(sellerId);
        SellerV2ApiResponse apiResponse = SellerV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
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
    @PutMapping("/{sellerId}")
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
    @PatchMapping("/{sellerId}/approve")
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
    @PatchMapping("/{sellerId}/reject")
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
    @PatchMapping("/{sellerId}/suspend")
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
    @PatchMapping("/{sellerId}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteSeller(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        DeleteSellerCommand command = mapper.toDeleteCommand(sellerId);
        deleteSellerUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
