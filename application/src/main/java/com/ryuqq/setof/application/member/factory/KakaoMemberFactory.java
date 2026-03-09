package com.ryuqq.setof.application.member.factory;

import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.SocialIntegrationContext;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberName;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * KakaoMemberFactory - 카카오 소셜 로그인 팩토리.
 *
 * <p>카카오 OAuth2 인증 정보로 Member 도메인 객체와 부가 정보를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class KakaoMemberFactory {

    private static final String KAKAO = "KAKAO";

    /**
     * 카카오 신규 회원용 Member 도메인 객체를 생성합니다.
     *
     * @param command 카카오 로그인 Command
     * @return 신규 Member 도메인 객체
     */
    public Member createMember(KakaoLoginCommand command) {
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
     * 카카오 신규 회원 가입 부가 정보를 생성합니다.
     *
     * <p>카카오 로그인은 비밀번호가 없으므로 빈 문자열로 설정합니다.
     *
     * @param command 카카오 로그인 Command
     * @return 가입 부가 정보
     */
    public MemberRegistrationInfo createRegistrationInfo(KakaoLoginCommand command) {
        return new MemberRegistrationInfo("", KAKAO, command.socialPkId(), true, true, true);
    }

    /**
     * 소셜 통합 Context를 생성합니다.
     *
     * <p>기존 전화번호 회원에 카카오 소셜 정보를 통합할 때 사용합니다.
     *
     * @param userId 레거시 user_id
     * @param command 카카오 로그인 Command
     * @return 소셜 통합 Context
     */
    public SocialIntegrationContext createIntegrationContext(
            long userId, KakaoLoginCommand command) {
        return new SocialIntegrationContext(
                userId, KAKAO, command.socialPkId(), command.gender(), command.dateOfBirth());
    }
}
