package com.ryuqq.setof.application.member.service.command;

import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.facade.command.RegisterMemberFacade;
import com.ryuqq.setof.application.member.factory.command.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.command.RegisterMemberUseCase;
import com.ryuqq.setof.application.member.validator.MemberPolicyValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원가입 서비스 (LOCAL 회원 전용)
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReadManager로 핸드폰 번호 존재 여부 조회
 *   <li>MemberPolicyValidator로 중복 검증
 *   <li>MemberCommandFactory로 Member 도메인 생성 (비밀번호 해시 포함)
 *   <li>RegisterMemberFacade로 저장 + 토큰 발급 + 이벤트 발행
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberReadManager memberReadManager;
    private final MemberPolicyValidator memberPolicyValidator;
    private final MemberCommandFactory memberCommandFactory;
    private final RegisterMemberFacade registerMemberFacade;

    public RegisterMemberService(
            MemberReadManager memberReadManager,
            MemberPolicyValidator memberPolicyValidator,
            MemberCommandFactory memberCommandFactory,
            RegisterMemberFacade registerMemberFacade) {
        this.memberReadManager = memberReadManager;
        this.memberPolicyValidator = memberPolicyValidator;
        this.memberCommandFactory = memberCommandFactory;
        this.registerMemberFacade = registerMemberFacade;
    }

    @Override
    public RegisterMemberResponse execute(RegisterMemberCommand command) {
        boolean exists = memberReadManager.existsByPhoneNumber(command.phoneNumber());
        memberPolicyValidator.validatePhoneNumberNotDuplicate(command.phoneNumber(), exists);

        Member member = memberCommandFactory.create(command);

        return registerMemberFacade.persistMember(member);
    }
}
