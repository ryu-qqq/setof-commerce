package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.application.member.port.in.command.WithdrawMemberUseCase;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원 탈퇴 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReader로 회원 조회
 *   <li>MemberPolicyValidator로 탈퇴 가능 여부 검증
 *   <li>MemberUpdater로 탈퇴 처리 (상태 변경 + 탈퇴 사유 저장)
 *   <li>MemberPersistenceManager로 저장
 *   <li>TokenManager로 Refresh Token 무효화
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class WithdrawMemberService implements WithdrawMemberUseCase {

    private final MemberReader memberReader;
    private final MemberPolicyValidator memberPolicyValidator;
    private final MemberUpdater memberUpdater;
    private final MemberPersistenceManager memberPersistenceManager;
    private final TokenManager tokenManager;

    public WithdrawMemberService(
            MemberReader memberReader,
            MemberPolicyValidator memberPolicyValidator,
            MemberUpdater memberUpdater,
            MemberPersistenceManager memberPersistenceManager,
            TokenManager tokenManager) {
        this.memberReader = memberReader;
        this.memberPolicyValidator = memberPolicyValidator;
        this.memberUpdater = memberUpdater;
        this.memberPersistenceManager = memberPersistenceManager;
        this.tokenManager = tokenManager;
    }

    @Override
    public void execute(WithdrawMemberCommand command) {
        Member member = memberReader.getById(command.memberId());

        memberPolicyValidator.validateCanWithdraw(member);

        memberUpdater.withdraw(member, command.reason());

        memberPersistenceManager.persist(member);

        tokenManager.revokeTokensByMemberId(command.memberId());
    }
}
