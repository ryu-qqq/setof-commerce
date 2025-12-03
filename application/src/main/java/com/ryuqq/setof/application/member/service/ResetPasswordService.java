package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.port.in.command.ResetPasswordUseCase;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 비밀번호 재설정 서비스
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>MemberReader로 회원 조회
 *   <li>MemberPolicyValidator로 비밀번호 재설정 가능 여부 검증
 *   <li>MemberUpdater로 비밀번호 변경
 *   <li>MemberPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final MemberReader memberReader;
    private final MemberPolicyValidator memberPolicyValidator;
    private final MemberUpdater memberUpdater;
    private final MemberPersistenceManager memberPersistenceManager;

    public ResetPasswordService(
            MemberReader memberReader,
            MemberPolicyValidator memberPolicyValidator,
            MemberUpdater memberUpdater,
            MemberPersistenceManager memberPersistenceManager) {
        this.memberReader = memberReader;
        this.memberPolicyValidator = memberPolicyValidator;
        this.memberUpdater = memberUpdater;
        this.memberPersistenceManager = memberPersistenceManager;
    }

    @Override
    public void execute(ResetPasswordCommand command) {
        Member member = memberReader.getByPhoneNumber(command.phoneNumber());

        memberPolicyValidator.validateCanResetPassword(member);

        memberUpdater.changePassword(member, command.newRawPassword());

        memberPersistenceManager.persist(member);
    }
}
