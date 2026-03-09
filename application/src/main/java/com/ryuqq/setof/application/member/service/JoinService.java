package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.factory.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.port.in.JoinUseCase;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원 가입 서비스.
 *
 * <p>JoinUseCase를 구현하며, 회원 등록 후 자동 로그인하여 토큰을 발급합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class JoinService implements JoinUseCase {

    private final MemberValidator memberValidator;
    private final MemberCommandFactory memberCommandFactory;
    private final MemberCommandManager memberCommandManager;
    private final TokenCommandFacade tokenCommandFacade;

    public JoinService(
            MemberValidator memberValidator,
            MemberCommandFactory memberCommandFactory,
            MemberCommandManager memberCommandManager,
            TokenCommandFacade tokenCommandFacade) {
        this.memberValidator = memberValidator;
        this.memberCommandFactory = memberCommandFactory;
        this.memberCommandManager = memberCommandManager;
        this.tokenCommandFacade = tokenCommandFacade;
    }

    @Override
    public LoginResult execute(JoinCommand command) {
        memberValidator.validateNotRegistered(command.phoneNumber());

        Member member = memberCommandFactory.createMember(command);
        MemberRegistrationInfo registrationInfo =
                memberCommandFactory.createRegistrationInfo(command);

        Long userId = memberCommandManager.persist(member, registrationInfo);
        return tokenCommandFacade.issueLoginResult(userId);
    }
}
