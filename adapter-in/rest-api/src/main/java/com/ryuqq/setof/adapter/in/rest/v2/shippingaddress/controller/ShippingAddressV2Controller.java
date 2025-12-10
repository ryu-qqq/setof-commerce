package com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.RegisterShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.UpdateShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.response.ShippingAddressV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.mapper.ShippingAddressV2ApiMapper;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.SetDefaultShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.SetDefaultShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ShippingAddress V2 Controller
 *
 * <p>배송지 관련 API 엔드포인트 (등록/조회/수정/삭제/기본설정)
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
@Tag(name = "ShippingAddress", description = "배송지 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ShippingAddresses.BASE)
@Validated
public class ShippingAddressV2Controller {

    private final RegisterShippingAddressUseCase registerShippingAddressUseCase;
    private final GetShippingAddressesUseCase getShippingAddressesUseCase;
    private final GetShippingAddressUseCase getShippingAddressUseCase;
    private final UpdateShippingAddressUseCase updateShippingAddressUseCase;
    private final DeleteShippingAddressUseCase deleteShippingAddressUseCase;
    private final SetDefaultShippingAddressUseCase setDefaultShippingAddressUseCase;
    private final ShippingAddressV2ApiMapper shippingAddressV2ApiMapper;

    public ShippingAddressV2Controller(
            RegisterShippingAddressUseCase registerShippingAddressUseCase,
            GetShippingAddressesUseCase getShippingAddressesUseCase,
            GetShippingAddressUseCase getShippingAddressUseCase,
            UpdateShippingAddressUseCase updateShippingAddressUseCase,
            DeleteShippingAddressUseCase deleteShippingAddressUseCase,
            SetDefaultShippingAddressUseCase setDefaultShippingAddressUseCase,
            ShippingAddressV2ApiMapper shippingAddressV2ApiMapper) {
        this.registerShippingAddressUseCase = registerShippingAddressUseCase;
        this.getShippingAddressesUseCase = getShippingAddressesUseCase;
        this.getShippingAddressUseCase = getShippingAddressUseCase;
        this.updateShippingAddressUseCase = updateShippingAddressUseCase;
        this.deleteShippingAddressUseCase = deleteShippingAddressUseCase;
        this.setDefaultShippingAddressUseCase = setDefaultShippingAddressUseCase;
        this.shippingAddressV2ApiMapper = shippingAddressV2ApiMapper;
    }

    /**
     * 배송지 목록 조회
     *
     * @param principal 인증된 사용자 정보
     * @return 배송지 목록 (최근 등록순)
     */
    @Operation(summary = "배송지 목록 조회", description = "현재 로그인한 회원의 배송지 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingAddressV2ApiResponse>>> getShippingAddresses(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        List<ShippingAddressResponse> responses = getShippingAddressesUseCase.execute(memberId);

        List<ShippingAddressV2ApiResponse> apiResponses =
                responses.stream().map(ShippingAddressV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * 배송지 단건 조회
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @return 배송지 정보
     */
    @Operation(summary = "배송지 단건 조회", description = "배송지 ID로 배송지 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송지 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.ShippingAddresses.ID_PATH)
    public ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> getShippingAddress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "배송지 ID", example = "1") @PathVariable Long shippingAddressId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        ShippingAddressResponse response =
                getShippingAddressUseCase.execute(memberId, shippingAddressId);

        ShippingAddressV2ApiResponse apiResponse = ShippingAddressV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 배송지 등록
     *
     * <p>회원당 최대 5개까지 등록 가능합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 배송지 등록 요청
     * @return 등록된 배송지 정보
     */
    @Operation(
            summary = "배송지 등록",
            description = "새로운 배송지를 등록합니다. 회원당 최대 5개까지 등록 가능합니다.")
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
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "배송지 개수 초과",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> registerShippingAddress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody RegisterShippingAddressV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        RegisterShippingAddressCommand command =
                shippingAddressV2ApiMapper.toRegisterCommand(memberId, request);

        ShippingAddressResponse response = registerShippingAddressUseCase.execute(command);
        ShippingAddressV2ApiResponse apiResponse = ShippingAddressV2ApiResponse.from(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 배송지 수정
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @param request 배송지 수정 요청
     * @return 수정된 배송지 정보
     */
    @Operation(summary = "배송지 수정", description = "배송지 정보를 수정합니다.")
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
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송지 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.ShippingAddresses.ID_PATH)
    public ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> updateShippingAddress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "배송지 ID", example = "1") @PathVariable Long shippingAddressId,
            @Valid @RequestBody UpdateShippingAddressV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        UpdateShippingAddressCommand command =
                shippingAddressV2ApiMapper.toUpdateCommand(memberId, shippingAddressId, request);

        ShippingAddressResponse response = updateShippingAddressUseCase.execute(command);
        ShippingAddressV2ApiResponse apiResponse = ShippingAddressV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 배송지 삭제 (Soft Delete)
     *
     * <p>기본 배송지 삭제 시 가장 최근 등록 배송지로 자동 변경됩니다.
     *
     * <p>컨벤션: DELETE 메서드 금지, 소프트 삭제는 PATCH로 처리
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @return 성공 응답
     */
    @Operation(
            summary = "배송지 삭제",
            description =
                    "배송지를 삭제합니다 (소프트 삭제). 기본 배송지 삭제 시 가장 최근 등록 배송지로 자동 변경됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송지 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.ShippingAddresses.ID_PATH + ApiV2Paths.ShippingAddresses.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteShippingAddress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "배송지 ID", example = "1") @PathVariable Long shippingAddressId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        DeleteShippingAddressCommand command =
                shippingAddressV2ApiMapper.toDeleteCommand(memberId, shippingAddressId);

        deleteShippingAddressUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 기본 배송지 설정
     *
     * <p>기존 기본 배송지가 있으면 해제 후 새로 설정됩니다.
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @return 설정된 배송지 정보
     */
    @Operation(
            summary = "기본 배송지 설정",
            description = "배송지를 기본 배송지로 설정합니다. 기존 기본 배송지가 있으면 해제됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송지 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.ShippingAddresses.ID_PATH + ApiV2Paths.ShippingAddresses.DEFAULT_PATH)
    public ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> setDefaultShippingAddress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "배송지 ID", example = "1") @PathVariable Long shippingAddressId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        SetDefaultShippingAddressCommand command =
                shippingAddressV2ApiMapper.toSetDefaultCommand(memberId, shippingAddressId);

        ShippingAddressResponse response = setDefaultShippingAddressUseCase.execute(command);
        ShippingAddressV2ApiResponse apiResponse = ShippingAddressV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
