package com.ryuqq.setof.application.member.factory;

import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberName;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * MemberCommandFactory - 회원 명령 Context/도메인 팩토리.
 *
 * <p>서비스에서 사용할 Member 도메인 객체와 Context를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class MemberCommandFactory {

    private final PasswordManager passwordManager;

    public MemberCommandFactory(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    /**
     * 회원 가입용 Member 도메인 객체를 생성합니다.
     *
     * @param command 가입 Command
     * @return 신규 Member 도메인 객체
     */
    public Member createMember(JoinCommand command) {
        return Member.forNew(
                null,
                MemberName.of(command.name()),
                null,
                PhoneNumber.of(command.phoneNumber()),
                null,
                null,
                Instant.now());
    }

    /**
     * 회원 가입 부가 정보를 생성합니다.
     *
     * @param command 가입 Command
     * @return 가입 부가 정보 (비밀번호 인코딩 포함)
     */
    public MemberRegistrationInfo createRegistrationInfo(JoinCommand command) {
        String encodedPassword = passwordManager.encode(command.password());
        return new MemberRegistrationInfo(
                encodedPassword,
                command.socialLoginType(),
                command.socialPkId(),
                command.privacyConsent(),
                command.serviceTermsConsent(),
                command.adConsent());
    }

    /**
     * 비밀번호 변경 Context 생성.
     *
     * @param member 회원 도메인 객체
     * @param rawPassword 평문 새 비밀번호
     * @return 비밀번호 변경 Context
     */
    public UpdatePasswordContext createUpdatePasswordContext(Member member, String rawPassword) {
        String encodedPassword = passwordManager.encode(rawPassword);
        return new UpdatePasswordContext(member.legacyMemberIdValue(), encodedPassword);
    }

    /**
     * 상태 변경 Context 생성 (탈퇴).
     *
     * @param member 회원 도메인 객체
     * @param withdrawalReason 탈퇴 사유
     * @return 상태 변경 Context
     */
    public StatusChangeContext createStatusChangeContext(Member member, String withdrawalReason) {
        return new StatusChangeContext(member.legacyMemberIdValue(), withdrawalReason);
    }
}
