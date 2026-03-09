package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.factory.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.port.in.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 비밀번호 재설정 서비스.
 *
 * <p>ResetPasswordUseCase를 구현하며, Validator로 회원 조회 후 Factory로 Context를 만들어 persist합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final MemberValidator memberValidator;
    private final MemberCommandFactory memberCommandFactory;
    private final MemberCommandManager memberCommandManager;

    public ResetPasswordService(
            MemberValidator memberValidator,
            MemberCommandFactory memberCommandFactory,
            MemberCommandManager memberCommandManager) {
        this.memberValidator = memberValidator;
        this.memberCommandFactory = memberCommandFactory;
        this.memberCommandManager = memberCommandManager;
    }

    @Override
    public void execute(ResetPasswordCommand command) {
        Member member = memberValidator.getByPhoneNumber(command.phoneNumber());

        UpdatePasswordContext context =
                memberCommandFactory.createUpdatePasswordContext(member, command.newPassword());

        memberCommandManager.persist(context);
    }
}
