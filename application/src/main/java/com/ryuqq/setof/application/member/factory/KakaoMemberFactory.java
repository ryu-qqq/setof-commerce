package com.ryuqq.setof.application.member.factory;

import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.ProviderUserId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * KakaoMemberFactory - 카카오 소셜 로그인 팩토리.
 *
 * <p>카카오 OAuth2 인증 정보로 MemberRegistrationBundle(도메인 객체 묶음)을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class KakaoMemberFactory {

    /**
     * 카카오 신규 회원 등록 번들을 생성합니다.
     *
     * <p>카카오 로그인은 비밀번호가 없으므로 forSocialAuth로 MemberAuth를 생성합니다.
     *
     * @param command 카카오 로그인 Command
     * @return 회원 등록 번들
     */
    public MemberRegistrationBundle createRegistrationBundle(KakaoLoginCommand command) {
        Instant now = Instant.now();

        Member member =
                Member.forNew(
                        MemberName.of(command.name()),
                        null,
                        PhoneNumber.of(command.phoneNumber()),
                        null,
                        null,
                        now);

        MemberAuth auth =
                MemberAuth.forSocialAuth(
                        null, AuthProvider.KAKAO, ProviderUserId.of(command.socialPkId()), now);

        MemberConsent consent = MemberConsent.forNew(null, true, true, true, now);

        return new MemberRegistrationBundle(member, auth, consent);
    }

    /**
     * 소셜 통합용 MemberAuth를 생성합니다.
     *
     * <p>기존 전화번호 회원에 카카오 인증 수단을 추가할 때 사용합니다.
     *
     * @param memberId 기존 회원 PK
     * @param command 카카오 로그인 Command
     * @return 소셜 인증 수단 도메인 객체
     */
    public MemberAuth createSocialAuth(Long memberId, KakaoLoginCommand command) {
        return MemberAuth.forSocialAuth(
                MemberId.of(memberId),
                AuthProvider.KAKAO,
                ProviderUserId.of(command.socialPkId()),
                Instant.now());
    }
}
