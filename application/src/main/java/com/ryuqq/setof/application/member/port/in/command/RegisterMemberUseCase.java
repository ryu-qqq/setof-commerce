package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;

/**
 * Register Member UseCase (Command)
 *
 * <p>회원가입을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>핸드폰 번호 중복 확인
 *   <li>비밀번호 BCrypt 해시화
 *   <li>Member 생성 (provider: LOCAL)
 *   <li>Member 저장 (트랜잭션)
 *   <li>JWT 토큰 발급 (트랜잭션 외부)
 *   <li>도메인 이벤트 발행
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterMemberUseCase {

    /**
     * 회원가입 실행
     *
     * @param command 회원가입 명령
     * @return 회원가입 응답 (memberId, tokens)
     */
    RegisterMemberResponse execute(RegisterMemberCommand command);
}
