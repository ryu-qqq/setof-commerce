package com.ryuqq.setof.adapter.in.rest.v1.auth.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.JoinV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.LoginV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.ResetPasswordV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request.WithdrawalV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.IsExistUserV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.LoginV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.MyPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.UserV1ApiResponse;
import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.command.LogoutCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.WithdrawalCommand;
import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import org.springframework.stereotype.Component;

/**
 * AuthV1ApiMapper - 인증/회원 V1 Public API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-004: API Request → Application Command 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class AuthV1ApiMapper {

    public LoginCommand toLoginCommand(LoginV1ApiRequest request) {
        return new LoginCommand(request.phoneNumber(), request.passwordHash());
    }

    public JoinCommand toJoinCommand(JoinV1ApiRequest request) {
        return new JoinCommand(
                request.phoneNumber(),
                request.passwordHash(),
                request.name(),
                "EMAIL",
                "",
                request.privacyConsent(),
                request.serviceTermsConsent(),
                request.adConsent());
    }

    public WithdrawalCommand toWithdrawalCommand(long userId, WithdrawalV1ApiRequest request) {
        return new WithdrawalCommand(userId, request.reason());
    }

    public ResetPasswordCommand toResetPasswordCommand(ResetPasswordV1ApiRequest request) {
        return new ResetPasswordCommand(request.phoneNumber(), request.passwordHash());
    }

    public LogoutCommand toLogoutCommand(long userId) {
        return new LogoutCommand(String.valueOf(userId));
    }

    public LoginV1ApiResponse toLoginResponse(LoginResult result) {
        return new LoginV1ApiResponse(result.accessToken(), result.refreshToken());
    }

    public IsExistUserV1ApiResponse toIsExistUserResponse(IsExistUserResult result) {
        if (!result.joined()) {
            return new IsExistUserV1ApiResponse(false, null);
        }
        return new IsExistUserV1ApiResponse(
                true,
                new IsExistUserV1ApiResponse.JoinedUserResponse(result.name(), result.userId()));
    }

    public UserV1ApiResponse toUserResponse(UserResult result) {
        return new UserV1ApiResponse(
                true,
                new UserV1ApiResponse.UserDetailResponse(
                        result.userId(),
                        result.phoneNumber(),
                        result.name(),
                        result.gradeName(),
                        result.currentMileage(),
                        result.socialLoginType()));
    }

    public MyPageV1ApiResponse toMyPageResponse(MyPageResult result) {
        return new MyPageV1ApiResponse(
                result.name(),
                result.phoneNumber(),
                result.email(),
                result.socialLoginType(),
                result.gradeName(),
                result.currentMileage(),
                result.orderCounts().stream()
                        .map(
                                oc ->
                                        new MyPageV1ApiResponse.OrderCountResponse(
                                                oc.orderStatus(), oc.count()))
                        .toList());
    }
}
