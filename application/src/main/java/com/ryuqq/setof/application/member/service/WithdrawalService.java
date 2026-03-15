package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.command.WithdrawalCommand;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.port.in.WithdrawalUseCase;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 탈퇴 서비스.
 *
 * <p>WithdrawalUseCase를 구현하며, 회원 도메인 객체를 조회 → 탈퇴 상태 변경 → persist합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class WithdrawalService implements WithdrawalUseCase {

    private final MemberValidator memberValidator;
    private final MemberCommandManager memberCommandManager;

    public WithdrawalService(
            MemberValidator memberValidator, MemberCommandManager memberCommandManager) {
        this.memberValidator = memberValidator;
        this.memberCommandManager = memberCommandManager;
    }

    @Override
    @Transactional
    public void execute(WithdrawalCommand command) {
        Member member = memberValidator.getById(command.userId());
        member.withdraw(Instant.now());
        memberCommandManager.persist(member);
    }
}
