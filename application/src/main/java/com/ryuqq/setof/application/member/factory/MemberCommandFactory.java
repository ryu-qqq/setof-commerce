package com.ryuqq.setof.application.member.factory;

import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import com.ryuqq.setof.domain.member.vo.ProviderUserId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * MemberCommandFactory - 회원 명령 팩토리.
 *
 * <p>서비스에서 사용할 MemberRegistrationBundle(도메인 객체 묶음)을 생성합니다.
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
     * 회원 가입 번들을 생성합니다.
     *
     * <p>Member, MemberAuth, MemberConsent 도메인 객체를 생성하여 번들로 묶습니다. memberId는 auto-increment로 DB에서
     * 할당되므로 null입니다. Facade에서 Member persist 후 할당합니다.
     *
     * @param command 가입 Command
     * @return 회원 등록 번들
     */
    public MemberRegistrationBundle createRegistrationBundle(JoinCommand command) {
        Instant now = Instant.now();
        String encodedPassword = passwordManager.encode(command.password());

        Member member =
                Member.forNew(
                        MemberName.of(command.name()),
                        null,
                        PhoneNumber.of(command.phoneNumber()),
                        null,
                        null,
                        now);

        MemberAuth auth =
                MemberAuth.forPhoneAuth(
                        null,
                        ProviderUserId.of(command.phoneNumber()),
                        PasswordHash.of(encodedPassword),
                        now);

        MemberConsent consent =
                MemberConsent.forNew(
                        null,
                        command.privacyConsent(),
                        command.serviceTermsConsent(),
                        command.adConsent(),
                        now);

        return new MemberRegistrationBundle(member, auth, consent);
    }
}
