package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.WithdrawalCommand;
import com.ryuqq.setof.application.member.factory.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.port.in.WithdrawalUseCase;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원 탈퇴 서비스.
 *
 * <p>WithdrawalUseCase를 구현하며, Validator로 회원 조회 후 Factory로 Context를 만들어 persist합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class WithdrawalService implements WithdrawalUseCase {

    private final MemberValidator memberValidator;
    private final MemberCommandFactory memberCommandFactory;
    private final MemberCommandManager memberCommandManager;

    public WithdrawalService(
            MemberValidator memberValidator,
            MemberCommandFactory memberCommandFactory,
            MemberCommandManager memberCommandManager) {
        this.memberValidator = memberValidator;
        this.memberCommandFactory = memberCommandFactory;
        this.memberCommandManager = memberCommandManager;
    }

    @Override
    public void execute(WithdrawalCommand command) {
        Member member = memberValidator.getByLegacyId(command.userId());

        StatusChangeContext context =
                memberCommandFactory.createStatusChangeContext(member, command.withdrawalReason());

        memberCommandManager.persist(context);
    }
}
