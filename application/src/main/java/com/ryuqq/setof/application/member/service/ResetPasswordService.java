package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.manager.MemberAuthCommandManager;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.application.member.port.in.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.port.out.query.MemberAuthQueryPort;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 비밀번호 재설정 서비스.
 *
 * <p>ResetPasswordUseCase를 구현하며, 인증 수단을 조회 → 비밀번호 변경 → persist합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final MemberValidator memberValidator;
    private final MemberAuthQueryPort memberAuthQueryPort;
    private final MemberAuthCommandManager memberAuthCommandManager;
    private final PasswordManager passwordManager;

    public ResetPasswordService(
            MemberValidator memberValidator,
            MemberAuthQueryPort memberAuthQueryPort,
            MemberAuthCommandManager memberAuthCommandManager,
            PasswordManager passwordManager) {
        this.memberValidator = memberValidator;
        this.memberAuthQueryPort = memberAuthQueryPort;
        this.memberAuthCommandManager = memberAuthCommandManager;
        this.passwordManager = passwordManager;
    }

    @Override
    @Transactional
    public void execute(ResetPasswordCommand command) {
        Member member = memberValidator.getByPhoneNumber(command.phoneNumber());
        MemberAuth auth = memberAuthQueryPort.findPhoneAuthByMemberId(member.idValue());

        String encodedPassword = passwordManager.encode(command.newPassword());
        auth.changePassword(PasswordHash.of(encodedPassword), Instant.now());

        memberAuthCommandManager.persist(auth);
    }
}
