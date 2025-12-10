package com.ryuqq.setof.application.member.factory.command;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderPort;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.Email;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.Password;
import com.ryuqq.setof.domain.member.vo.SocialId;
import com.ryuqq.setof.domain.member.vo.WithdrawalReason;
import java.util.Locale;
import org.springframework.stereotype.Component;

/**
 * Member Update Factory
 *
 * <p>회원 정보 업데이트 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command를 기반으로 도메인 객체 상태 변경 준비
 *   <li>비밀번호 인코딩 등 외부 의존성 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberUpdateFactory {

    private final PasswordEncoderPort passwordEncoderPort;
    private final ClockHolder clockHolder;

    public MemberUpdateFactory(PasswordEncoderPort passwordEncoderPort, ClockHolder clockHolder) {
        this.passwordEncoderPort = passwordEncoderPort;
        this.clockHolder = clockHolder;
    }

    /**
     * 비밀번호 변경
     *
     * @param member 대상 회원
     * @param newRawPassword 새 평문 비밀번호
     */
    public void changePassword(Member member, String newRawPassword) {
        String hashedPassword = passwordEncoderPort.encode(newRawPassword);
        Password newPassword = Password.of(hashedPassword);

        member.changePassword(newPassword, clockHolder.getClock());
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param member 대상 회원
     * @param reason 탈퇴 사유
     */
    public void withdraw(Member member, String reason) {
        WithdrawalReason withdrawalReason = WithdrawalReason.valueOf(reason);
        member.withdraw(withdrawalReason, clockHolder.getClock());
    }

    /**
     * 카카오 계정 연동 및 프로필 업데이트
     *
     * <p>카카오에서 받은 프로필 정보로 회원 정보를 업데이트합니다.
     *
     * @param member 대상 회원
     * @param command 카카오 통합 커맨드
     */
    public void linkKakaoWithProfile(Member member, IntegrateKakaoCommand command) {
        SocialId kakaoSocialId = SocialId.of(command.kakaoId());
        Email email = command.hasEmail() ? Email.of(command.email()) : null;
        MemberName name = command.hasName() ? MemberName.of(command.name()) : null;
        Gender gender = command.hasGender() ? toGender(command.gender()) : null;

        member.linkKakaoWithProfile(
                kakaoSocialId, email, name, command.dateOfBirth(), gender, clockHolder.getClock());
    }

    private Gender toGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return Gender.N;
        }
        return Gender.valueOf(gender.toUpperCase(Locale.ROOT));
    }
}
