package com.ryuqq.setof.adapter.in.rest.v1.auth.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.auth.AuthV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.JoinV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.LoginV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.ResetPasswordV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.WithdrawalV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.LoginV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.mapper.AuthV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.port.in.LoginUseCase;
import com.ryuqq.setof.application.auth.port.in.LogoutUseCase;
import com.ryuqq.setof.application.member.port.in.JoinUseCase;
import com.ryuqq.setof.application.member.port.in.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.port.in.WithdrawalUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthCommandV1Controller - 인증 명령 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Tag(name = "인증 명령 V1", description = "로그인/회원가입/로그아웃/탈퇴/비밀번호 V1 Public API")
@RestController
public class AuthCommandV1Controller {

    private final LoginUseCase loginUseCase;
    private final JoinUseCase joinUseCase;
    private final LogoutUseCase logoutUseCase;
    private final WithdrawalUseCase withdrawalUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final AuthV1ApiMapper mapper;

    public AuthCommandV1Controller(
            LoginUseCase loginUseCase,
            JoinUseCase joinUseCase,
            LogoutUseCase logoutUseCase,
            WithdrawalUseCase withdrawalUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            AuthV1ApiMapper mapper) {
        this.loginUseCase = loginUseCase;
        this.joinUseCase = joinUseCase;
        this.logoutUseCase = logoutUseCase;
        this.withdrawalUseCase = withdrawalUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 실패")
    })
    @PostMapping(AuthV1Endpoints.LOGIN)
    public ResponseEntity<V1ApiResponse<LoginV1ApiResponse>> login(
            @Valid @RequestBody LoginV1ApiRequest request) {
        LoginResult result = loginUseCase.execute(mapper.toLoginCommand(request));
        return ResponseEntity.ok(V1ApiResponse.success(mapper.toLoginResponse(result)));
    }

    @Operation(summary = "회원가입", description = "신규 회원을 등록하고 자동 로그인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "가입 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패 또는 이미 가입된 번호")
    })
    @PostMapping(AuthV1Endpoints.JOIN)
    public ResponseEntity<V1ApiResponse<LoginV1ApiResponse>> join(
            @Valid @RequestBody JoinV1ApiRequest request) {
        LoginResult result = joinUseCase.execute(mapper.toJoinCommand(request));
        return ResponseEntity.ok(V1ApiResponse.success(mapper.toLoginResponse(result)));
    }

    @Operation(summary = "로그아웃", description = "현재 세션의 토큰을 무효화합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그아웃 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(AuthV1Endpoints.LOGOUT)
    public ResponseEntity<V1ApiResponse<Void>> logout(@AuthenticatedUserId Long userId) {
        logoutUseCase.execute(mapper.toLogoutCommand(userId));
        return ResponseEntity.ok(V1ApiResponse.success());
    }

    @Operation(summary = "회원탈퇴", description = "회원 탈퇴를 처리합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "탈퇴 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(AuthV1Endpoints.WITHDRAWAL)
    public ResponseEntity<V1ApiResponse<Void>> withdrawal(
            @AuthenticatedUserId Long userId, @Valid @RequestBody WithdrawalV1ApiRequest request) {
        withdrawalUseCase.execute(mapper.toWithdrawalCommand(userId, request));
        return ResponseEntity.ok(V1ApiResponse.success());
    }

    @Operation(summary = "비밀번호 재설정", description = "전화번호로 비밀번호를 재설정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "재설정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패")
    })
    @PatchMapping(AuthV1Endpoints.RESET_PASSWORD)
    public ResponseEntity<V1ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordV1ApiRequest request) {
        resetPasswordUseCase.execute(mapper.toResetPasswordCommand(request));
        return ResponseEntity.ok(V1ApiResponse.success());
    }
}
