package com.ryuqq.setof.application.member.component;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.port.out.PasswordEncoderPort;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import java.time.Clock;
import org.springframework.stereotype.Component;

/**
 * Member Creator
 *
 * <p>회원 생성 전용 컴포넌트
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberCreator {

    private final PasswordEncoderPort passwordEncoderPort;
    private final MemberAssembler memberAssembler;
    private final Clock clock;

    public MemberCreator(
            PasswordEncoderPort passwordEncoderPort, MemberAssembler memberAssembler, Clock clock) {
        this.passwordEncoderPort = passwordEncoderPort;
        this.memberAssembler = memberAssembler;
        this.clock = clock;
    }

    /**
     * 로컬 회원 생성
     *
     * @param command 회원가입 커맨드
     * @return 생성된 Member (저장 전)
     */
    public Member createLocalMember(RegisterMemberCommand command) {
        String hashedPassword = passwordEncoderPort.encode(command.rawPassword());
        return memberAssembler.toDomain(command, hashedPassword, clock);
    }

    /**
     * 카카오 회원 생성
     *
     * @param command 카카오 로그인 커맨드
     * @return 생성된 Member (저장 전)
     */
    public Member createKakaoMember(KakaoOAuthCommand command) {
        return memberAssembler.toKakaoDomain(command, clock);
    }
}
