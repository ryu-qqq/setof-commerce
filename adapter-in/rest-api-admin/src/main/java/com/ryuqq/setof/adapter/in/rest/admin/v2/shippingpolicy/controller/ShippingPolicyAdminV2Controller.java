package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.response.ShippingPolicyV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchQuery;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.DeleteShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.SetDefaultShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPoliciesUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPolicyUseCase;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ShippingPolicy Admin V2 Controller
 *
 * <p>배송 정책 관리 API 엔드포인트 (등록/조회/수정/기본설정/삭제)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>Command/Query 분리 (CQRS)
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin ShippingPolicy", description = "배송 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ShippingPolicies.BASE)
@Validated
public class ShippingPolicyAdminV2Controller {

    private final RegisterShippingPolicyUseCase registerShippingPolicyUseCase;
    private final GetShippingPolicyUseCase getShippingPolicyUseCase;
    private final GetShippingPoliciesUseCase getShippingPoliciesUseCase;
    private final UpdateShippingPolicyUseCase updateShippingPolicyUseCase;
    private final SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase;
    private final DeleteShippingPolicyUseCase deleteShippingPolicyUseCase;
    private final ShippingPolicyAdminV2ApiMapper mapper;

    public ShippingPolicyAdminV2Controller(
            RegisterShippingPolicyUseCase registerShippingPolicyUseCase,
            GetShippingPolicyUseCase getShippingPolicyUseCase,
            GetShippingPoliciesUseCase getShippingPoliciesUseCase,
            UpdateShippingPolicyUseCase updateShippingPolicyUseCase,
            SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase,
            DeleteShippingPolicyUseCase deleteShippingPolicyUseCase,
            ShippingPolicyAdminV2ApiMapper mapper) {
        this.registerShippingPolicyUseCase = registerShippingPolicyUseCase;
        this.getShippingPolicyUseCase = getShippingPolicyUseCase;
        this.getShippingPoliciesUseCase = getShippingPoliciesUseCase;
        this.updateShippingPolicyUseCase = updateShippingPolicyUseCase;
        this.setDefaultShippingPolicyUseCase = setDefaultShippingPolicyUseCase;
        this.deleteShippingPolicyUseCase = deleteShippingPolicyUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "배송 정책 목록 조회", description = "셀러의 배송 정책 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingPolicyV2ApiResponse>>> getShippingPolicies(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "삭제된 정책 포함 여부") @RequestParam(defaultValue = "false")
                    boolean includeDeleted) {

        ShippingPolicySearchQuery query = mapper.toSearchQuery(sellerId, includeDeleted);
        List<ShippingPolicyResponse> responses = getShippingPoliciesUseCase.execute(query);

        List<ShippingPolicyV2ApiResponse> apiResponses =
                responses.stream().map(ShippingPolicyV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "배송 정책 상세 조회", description = "배송 정책 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.ShippingPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<ShippingPolicyV2ApiResponse>> getShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId) {

        ShippingPolicyResponse response = getShippingPolicyUseCase.execute(shippingPolicyId);
        ShippingPolicyV2ApiResponse apiResponse = ShippingPolicyV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    @Operation(summary = "배송 정책 등록", description = "새로운 배송 정책을 등록합니다.")
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
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Valid @RequestBody RegisterShippingPolicyV2ApiRequest request) {

        RegisterShippingPolicyCommand command = mapper.toRegisterCommand(request);
        Long shippingPolicyId = registerShippingPolicyUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(shippingPolicyId));
    }

    @Operation(summary = "배송 정책 수정", description = "배송 정책 정보를 수정합니다.")
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
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.ShippingPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId,
            @Valid @RequestBody UpdateShippingPolicyV2ApiRequest request) {

        UpdateShippingPolicyCommand command = mapper.toUpdateCommand(shippingPolicyId, request);
        updateShippingPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "기본 배송 정책 설정", description = "해당 배송 정책을 기본 정책으로 설정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.ShippingPolicies.DEFAULT_PATH)
    public ResponseEntity<ApiResponse<Void>> setDefaultShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId) {

        SetDefaultShippingPolicyCommand command =
                mapper.toSetDefaultCommand(shippingPolicyId, sellerId);
        setDefaultShippingPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "배송 정책 삭제", description = "배송 정책을 삭제합니다 (소프트 삭제).")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "삭제 불가 (기본 정책)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.ShippingPolicies.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId) {

        DeleteShippingPolicyCommand command = mapper.toDeleteCommand(shippingPolicyId, sellerId);
        deleteShippingPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
