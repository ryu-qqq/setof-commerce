package com.ryuqq.setof.adapter.in.rest.admin.v2.auth.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.LoginApiResponse;
import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.command.LogoutCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import org.springframework.stereotype.Component;

/**
 * AuthCommandApiMapper - 인증 Command Mapper.
 *
 * <p>API Request → Application Command, Application Result → API Response 변환을 담당합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-004: Command Mapper는 toCommand() 메서드 제공.
 *
 * <p>API-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class AuthCommandApiMapper {

    /**
     * LoginApiRequest → LoginCommand 변환.
     *
     * @param request API 요청
     * @return LoginCommand
     */
    public LoginCommand toCommand(LoginApiRequest request) {
        return new LoginCommand(request.identifier(), request.password());
    }

    /**
     * userId → LogoutCommand 변환.
     *
     * @param userId 사용자 ID
     * @return LogoutCommand
     */
    public LogoutCommand toCommand(String userId) {
        return new LogoutCommand(userId);
    }

    /**
     * LoginResult → LoginApiResponse 변환.
     *
     * <p>로그인 실패 시 IllegalArgumentException을 발생시킵니다.
     *
     * @param result 로그인 결과
     * @return LoginApiResponse
     * @throws IllegalArgumentException 로그인 실패 시
     */
    public LoginApiResponse toResponse(LoginResult result) {
        if (result.isFailure()) {
            throw new IllegalArgumentException(result.errorMessage());
        }

        return new LoginApiResponse(
                result.accessToken(),
                result.refreshToken(),
                result.tokenType(),
                result.expiresIn());
    }
}
