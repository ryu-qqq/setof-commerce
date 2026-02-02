package com.ryuqq.setof.adapter.in.rest.admin.v2.auth.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.AuthAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.LoginApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.mapper.AuthCommandApiMapper;
import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.port.in.LoginUseCase;
import com.ryuqq.setof.application.auth.port.in.LogoutUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthCommandController - 인증 Command API.
 *
 * <p>로그인/로그아웃 엔드포인트를 제공합니다.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-009: @Valid 어노테이션 필수.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "인증", description = "로그인/로그아웃 API")
@RestController
@RequestMapping(AuthAdminEndpoints.BASE)
public class AuthCommandController {

    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final AuthCommandApiMapper commandMapper;

    public AuthCommandController(
            LoginUseCase loginUseCase,
            LogoutUseCase logoutUseCase,
            AuthCommandApiMapper commandMapper) {
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
        this.commandMapper = commandMapper;
    }

    /**
     * 로그인 API.
     *
     * <p>사용자 인증 후 액세스 토큰을 발급합니다.
     *
     * @param request 로그인 요청 DTO
     * @return 액세스 토큰
     */
    @Operation(summary = "로그인", description = "사용자 인증 후 액세스 토큰을 발급합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 실패")
    })
    @PostMapping(AuthAdminEndpoints.LOGIN)
    public ResponseEntity<ApiResponse<LoginApiResponse>> login(
            @Valid @RequestBody LoginApiRequest request) {

        LoginCommand command = commandMapper.toCommand(request);
        LoginResult result = loginUseCase.execute(command);
        LoginApiResponse response = commandMapper.toResponse(result);

        return ResponseEntity.ok(ApiResponse.of(response));
    }

    /**
     * 로그아웃 API.
     *
     * <p>현재 세션의 토큰을 무효화합니다.
     *
     * @return 빈 응답
     */
    @Operation(summary = "로그아웃", description = "현재 세션의 토큰을 무효화합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그아웃 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 요청")
    })
    @PostMapping(AuthAdminEndpoints.LOGOUT)
    public ResponseEntity<ApiResponse<Void>> logout() {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        logoutUseCase.execute(commandMapper.toCommand(userId));

        return ResponseEntity.ok(ApiResponse.of());
    }
}
