package com.ryuqq.setof.application.member.service.command;

import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.command.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.validator.MemberPolicyValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 비밀번호 재설정 서비스
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>MemberReadManager로 회원 조회
 *   <li>MemberPolicyValidator로 비밀번호 재설정 가능 여부 검증
 *   <li>MemberUpdateFactory로 비밀번호 변경
 *   <li>MemberPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final MemberReadManager memberReadManager;
    private final MemberPolicyValidator memberPolicyValidator;
    private final MemberUpdateFactory memberUpdateFactory;
    private final MemberPersistenceManager memberPersistenceManager;

    public ResetPasswordService(
            MemberReadManager memberReadManager,
            MemberPolicyValidator memberPolicyValidator,
            MemberUpdateFactory memberUpdateFactory,
            MemberPersistenceManager memberPersistenceManager) {
        this.memberReadManager = memberReadManager;
        this.memberPolicyValidator = memberPolicyValidator;
        this.memberUpdateFactory = memberUpdateFactory;
        this.memberPersistenceManager = memberPersistenceManager;
    }

    @Override
    public void execute(ResetPasswordCommand command) {
        Member member = memberReadManager.findByPhoneNumber(command.phoneNumber());

        memberPolicyValidator.validateCanResetPassword(member);

        memberUpdateFactory.changePassword(member, command.newRawPassword());

        memberPersistenceManager.persist(member);
    }
}
