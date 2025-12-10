package com.ryuqq.setof.application.member.service.command;

import com.ryuqq.setof.application.auth.port.in.command.RevokeTokensUseCase;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.command.WithdrawMemberUseCase;
import com.ryuqq.setof.application.member.validator.MemberPolicyValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원 탈퇴 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReadManager로 회원 조회
 *   <li>MemberPolicyValidator로 탈퇴 가능 여부 검증
 *   <li>MemberUpdateFactory로 탈퇴 처리 (상태 변경 + 탈퇴 사유 저장)
 *   <li>MemberPersistenceManager로 저장
 *   <li>RevokeTokensUseCase로 Refresh Token 무효화
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class WithdrawMemberService implements WithdrawMemberUseCase {

    private final MemberReadManager memberReadManager;
    private final MemberPolicyValidator memberPolicyValidator;
    private final MemberUpdateFactory memberUpdateFactory;
    private final MemberPersistenceManager memberPersistenceManager;
    private final RevokeTokensUseCase revokeTokensUseCase;

    public WithdrawMemberService(
            MemberReadManager memberReadManager,
            MemberPolicyValidator memberPolicyValidator,
            MemberUpdateFactory memberUpdateFactory,
            MemberPersistenceManager memberPersistenceManager,
            RevokeTokensUseCase revokeTokensUseCase) {
        this.memberReadManager = memberReadManager;
        this.memberPolicyValidator = memberPolicyValidator;
        this.memberUpdateFactory = memberUpdateFactory;
        this.memberPersistenceManager = memberPersistenceManager;
        this.revokeTokensUseCase = revokeTokensUseCase;
    }

    @Override
    public void execute(WithdrawMemberCommand command) {
        Member member = memberReadManager.findById(command.memberId());

        memberPolicyValidator.validateCanWithdraw(member);

        memberUpdateFactory.withdraw(member, command.reason());

        memberPersistenceManager.persist(member);

        revokeTokensUseCase.revokeByMemberId(command.memberId());
    }
}
