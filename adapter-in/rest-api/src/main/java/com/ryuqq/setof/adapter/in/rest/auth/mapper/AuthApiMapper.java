package com.ryuqq.setof.adapter.in.rest.auth.mapper;

import com.ryuqq.setof.adapter.in.rest.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import org.springframework.stereotype.Component;

/**
 * Auth API Mapper
 *
 * <p>인증 관련 API DTO ↔ Application Command/Response 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class AuthApiMapper {

    /**
     * 로그인 요청 → 로그인 커맨드 변환
     *
     * @param request API 요청
     * @return LocalLoginCommand
     */
    public LocalLoginCommand toLocalLoginCommand(LoginApiRequest request) {
        return new LocalLoginCommand(request.phoneNumber(), request.password());
    }

    /**
     * 로그아웃 커맨드 생성
     *
     * @param memberId 회원 ID
     * @return LogoutMemberCommand
     */
    public LogoutMemberCommand toLogoutCommand(String memberId) {
        return new LogoutMemberCommand(memberId);
    }

    /**
     * Application TokenPairResponse → TokenApiResponse 변환
     *
     * @param tokens Application 토큰 응답
     * @return TokenApiResponse
     */
    public TokenApiResponse toTokenApiResponse(TokenPairResponse tokens) {
        return TokenApiResponse.from(tokens);
    }
}
