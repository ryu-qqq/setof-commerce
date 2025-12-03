package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.component.MemberCreator;
import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.facade.RegisterMemberFacade;
import com.ryuqq.setof.application.member.port.in.command.RegisterMemberUseCase;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원가입 서비스 (LOCAL 회원 전용)
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReader로 핸드폰 번호 존재 여부 조회
 *   <li>MemberPolicyValidator로 중복 검증
 *   <li>MemberCreator로 Member 도메인 생성 (비밀번호 해시 포함)
 *   <li>RegisterMemberFacade로 저장 + 토큰 발급 + 이벤트 발행
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberReader memberReader;
    private final MemberPolicyValidator memberPolicyValidator;
    private final MemberCreator memberCreator;
    private final RegisterMemberFacade registerMemberFacade;

    public RegisterMemberService(
            MemberReader memberReader,
            MemberPolicyValidator memberPolicyValidator,
            MemberCreator memberCreator,
            RegisterMemberFacade registerMemberFacade) {
        this.memberReader = memberReader;
        this.memberPolicyValidator = memberPolicyValidator;
        this.memberCreator = memberCreator;
        this.registerMemberFacade = registerMemberFacade;
    }

    @Override
    public RegisterMemberResponse execute(RegisterMemberCommand command) {
        boolean exists = memberReader.existsByPhoneNumber(command.phoneNumber());
        memberPolicyValidator.validatePhoneNumberNotDuplicate(exists);

        Member member = memberCreator.createLocalMember(command);

        return registerMemberFacade.register(member);
    }
}
