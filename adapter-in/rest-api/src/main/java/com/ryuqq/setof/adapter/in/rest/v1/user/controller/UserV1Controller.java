package com.ryuqq.setof.adapter.in.rest.v1.user.controller;

import java.util.Optional;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.JoinV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.LoginV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.PasswordV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.WithdrawV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.query.SearchUserV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.response.UserV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.user.mapper.UserV1ApiMapper;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.port.in.command.LocalLoginUseCase;
import com.ryuqq.setof.application.member.port.in.command.LogoutMemberUseCase;
import com.ryuqq.setof.application.member.port.in.command.RegisterMemberUseCase;
import com.ryuqq.setof.application.member.port.in.command.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.port.in.command.WithdrawMemberUseCase;
import com.ryuqq.setof.application.member.port.in.query.FindMemberByPhoneUseCase;
import com.ryuqq.setof.application.member.port.in.query.GetCurrentMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * V1 User Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 User 엔드포인트 V2 UseCase를 재사용하며, V1 DTO로 변환하여 응답
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "User (Legacy V1)", description = "레거시 사용자 API - V2로 마이그레이션 권장")
@RestController
@RequestMapping
@Validated
@Deprecated
public class UserV1Controller {

    private final LocalLoginUseCase localLoginUseCase;
    private final LogoutMemberUseCase logoutMemberUseCase;
    private final RegisterMemberUseCase registerMemberUseCase;
    private final GetCurrentMemberUseCase getCurrentMemberUseCase;
    private final FindMemberByPhoneUseCase findMemberByPhoneUseCase;
    private final WithdrawMemberUseCase withdrawMemberUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final UserV1ApiMapper userV1ApiMapper;
    private final TokenCookieWriter tokenCookieWriter;

    public UserV1Controller(LocalLoginUseCase localLoginUseCase,
            LogoutMemberUseCase logoutMemberUseCase, RegisterMemberUseCase registerMemberUseCase,
            GetCurrentMemberUseCase getCurrentMemberUseCase,
            FindMemberByPhoneUseCase findMemberByPhoneUseCase,
            WithdrawMemberUseCase withdrawMemberUseCase, ResetPasswordUseCase resetPasswordUseCase,
            UserV1ApiMapper userV1ApiMapper, TokenCookieWriter tokenCookieWriter) {
        this.localLoginUseCase = localLoginUseCase;
        this.logoutMemberUseCase = logoutMemberUseCase;
        this.registerMemberUseCase = registerMemberUseCase;
        this.getCurrentMemberUseCase = getCurrentMemberUseCase;
        this.findMemberByPhoneUseCase = findMemberByPhoneUseCase;
        this.withdrawMemberUseCase = withdrawMemberUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.userV1ApiMapper = userV1ApiMapper;
        this.tokenCookieWriter = tokenCookieWriter;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 정보 조회", description = "현재 로그인한 사용자 정보를 조회합니다.")
    @GetMapping(ApiPaths.User.ME)
    public ResponseEntity<ApiResponse<UserV1ApiResponse>> me(
            @AuthenticationPrincipal MemberPrincipal principal) {

        MemberDetailResponse detail = getCurrentMemberUseCase
                .execute(userV1ApiMapper.toGetCurrentMemberQuery(principal.getMemberId()));

        return ResponseEntity.ok(ApiResponse.ofSuccess(userV1ApiMapper.toUserResponse(detail)));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 회원가입", description = "새로운 회원을 등록합니다.")
    @PostMapping(ApiPaths.User.JOIN)
    public ResponseEntity<ApiResponse<Void>> join(@Valid @RequestBody JoinV1ApiRequest request,
            HttpServletResponse response) {

        RegisterMemberResponse registerResponse =
                registerMemberUseCase.execute(userV1ApiMapper.toRegisterMemberCommand(request));

        TokenPairResponse tokens = registerResponse.tokens();
        tokenCookieWriter.addTokenCookies(response, tokens.accessToken(), tokens.refreshToken(),
                tokens.accessTokenExpiresIn(), tokens.refreshTokenExpiresIn());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess());
    }

    @Deprecated
    @Operation(summary = "[Legacy] 로그인", description = "핸드폰 번호와 비밀번호로 로그인합니다.")
    @PostMapping(ApiPaths.User.LOGIN)
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginV1ApiRequest request,
            HttpServletResponse response) {

        LocalLoginResponse loginResult =
                localLoginUseCase.execute(userV1ApiMapper.toLocalLoginCommand(request));

        TokenPairResponse tokens = loginResult.tokens();
        tokenCookieWriter.addTokenCookies(response, tokens.accessToken(), tokens.refreshToken(),
                tokens.accessTokenExpiresIn(), tokens.refreshTokenExpiresIn());

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Deprecated
    @Operation(summary = "[Legacy] 로그아웃", description = "로그아웃하고 토큰을 무효화합니다.")
    @PostMapping(ApiPaths.User.LOGOUT)
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal MemberPrincipal principal, HttpServletResponse response) {

        logoutMemberUseCase.execute(userV1ApiMapper.toLogoutMemberCommand(principal.getMemberId()));
        tokenCookieWriter.deleteTokenCookies(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Deprecated
    @Operation(summary = "[Legacy] 회원 탈퇴", description = "회원을 탈퇴 처리합니다.")
    @PostMapping(ApiPaths.User.WITHDRAWAL)
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody WithdrawV1ApiRequest request, HttpServletResponse response) {

        withdrawMemberUseCase
                .execute(userV1ApiMapper.toWithdrawMemberCommand(principal.getMemberId(), request));
        tokenCookieWriter.deleteTokenCookies(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Deprecated
    @Operation(summary = "[Legacy] 비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PatchMapping(ApiPaths.User.PASSWORD)
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody PasswordV1ApiRequest request) {

        resetPasswordUseCase.execute(
                userV1ApiMapper.toResetPasswordCommand(principal.getPhoneNumber(), request));

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Deprecated
    @Operation(summary = "[Legacy] 사용자 존재 확인", description = "핸드폰 번호로 사용자 존재 여부를 확인합니다.")
    @GetMapping(ApiPaths.User.EXISTS)
    public ResponseEntity<ApiResponse<UserV1ApiResponse>> isExistUser(
            @ModelAttribute @Validated SearchUserV1ApiRequest searchUserV1ApiRequest) {

        Optional<MemberDetailResponse> memberOptional = findMemberByPhoneUseCase
                .execute(userV1ApiMapper.toFindMemberByPhoneQuery(searchUserV1ApiRequest));

        UserV1ApiResponse response = memberOptional.map(userV1ApiMapper::toUserResponse)
                .orElse(new UserV1ApiResponse(false));

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

}
