package com.ryuqq.setof.application.member.factory.command;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderPort;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import org.springframework.stereotype.Component;

/**
 * Member Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberCommandFactory {

    private final PasswordEncoderPort passwordEncoderPort;
    private final MemberAssembler memberAssembler;
    private final ClockHolder clockHolder;

    public MemberCommandFactory(
            PasswordEncoderPort passwordEncoderPort,
            MemberAssembler memberAssembler,
            ClockHolder clockHolder) {
        this.passwordEncoderPort = passwordEncoderPort;
        this.memberAssembler = memberAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 로컬 회원 생성
     *
     * <p>UUID v7은 Application Layer에서 UuidCreator.getTimeOrderedEpoch()로 생성
     *
     * @param command 회원가입 커맨드
     * @return 생성된 Member (저장 전)
     */
    public Member create(RegisterMemberCommand command) {
        MemberId memberId = MemberId.forNew(UuidCreator.getTimeOrderedEpoch());
        String hashedPassword = passwordEncoderPort.encode(command.rawPassword());
        return memberAssembler.toDomain(memberId, command, hashedPassword, clockHolder.getClock());
    }

    /**
     * 카카오 회원 생성
     *
     * <p>UUID v7은 Application Layer에서 UuidCreator.getTimeOrderedEpoch()로 생성
     *
     * @param command 카카오 로그인 커맨드
     * @return 생성된 Member (저장 전)
     */
    public Member create(KakaoOAuthCommand command) {
        MemberId memberId = MemberId.forNew(UuidCreator.getTimeOrderedEpoch());
        return memberAssembler.toKakaoDomain(memberId, command, clockHolder.getClock());
    }
}
