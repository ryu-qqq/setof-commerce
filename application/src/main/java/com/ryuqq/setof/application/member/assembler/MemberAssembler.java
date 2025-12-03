package com.ryuqq.setof.application.member.assembler;

import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Consent;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberName;
import com.ryuqq.setof.domain.core.member.vo.Password;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.core.member.vo.SocialId;

import java.time.Clock;
import java.util.Locale;

import org.springframework.stereotype.Component;

/**
 * Member Assembler
 *
 * <p>DTO ↔ Domain 변환 전용. 비즈니스 로직 포함 금지.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberAssembler {

    /**
     * Command → Domain (신규 회원 생성)
     *
     * @param command 회원가입 커맨드
     * @param hashedPassword BCrypt 해시된 비밀번호
     * @param clock 시간 제공자
     * @return Member Domain 객체
     */
    public Member toDomain(RegisterMemberCommand command, String hashedPassword, Clock clock) {
        return Member.forNew(
                PhoneNumber.of(command.phoneNumber()),
                toEmail(command.email()),
                Password.of(hashedPassword),
                MemberName.of(command.name()),
                command.dateOfBirth(),
                toGender(command.gender()),
                AuthProvider.LOCAL,
                null,
                toConsent(command),
                clock);
    }

    /**
     * 로컬 로그인 응답 생성
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param tokens 발급된 토큰 쌍
     * @return 로그인 응답
     */
    public LocalLoginResponse toLocalLoginResponse(String memberId, TokenPairResponse tokens) {
        return new LocalLoginResponse(memberId, tokens);
    }

    /**
     * Domain → MemberDetailResponse 변환
     *
     * @param member Member 도메인 객체
     * @return 회원 상세 정보 응답
     */
    public MemberDetailResponse toMemberDetailResponse(Member member) {
        return new MemberDetailResponse(
                member.getIdValue(),
                member.getPhoneNumberValue(),
                member.getEmailValue(),
                member.getNameValue(),
                member.getDateOfBirth(),
                member.getGender().name(),
                member.getProvider().name(),
                member.getStatus().name(),
                member.getCreatedAt());
    }

    /**
     * 신규 카카오 회원 응답 생성
     *
     * @param memberId 회원 ID
     * @param tokens 발급된 토큰 쌍
     * @return 카카오 OAuth 응답
     */
    public KakaoOAuthResponse toNewKakaoMemberResponse(String memberId, TokenPairResponse tokens) {
        return KakaoOAuthResponse.newMember(memberId, tokens);
    }

    /**
     * 기존 카카오 회원 응답 생성
     *
     * @param memberId 회원 ID
     * @param tokens 발급된 토큰 쌍
     * @return 카카오 OAuth 응답
     */
    public KakaoOAuthResponse toExistingKakaoMemberResponse(String memberId, TokenPairResponse tokens) {
        return KakaoOAuthResponse.existingKakaoMember(memberId, tokens);
    }


    /**
     * KakaoOAuthCommand → Domain (카카오 회원 생성)
     *
     * @param command 카카오 로그인 커맨드
     * @param clock 시간 제공자
     * @return Member Domain 객체
     */
    public Member toKakaoDomain(KakaoOAuthCommand command, Clock clock) {
        return Member.forNew(
                PhoneNumber.of(command.phoneNumber()),
                toEmail(command.email()),
                null,
                toMemberName(command.name()),
                command.dateOfBirth(),
                toGender(command.gender()),
                AuthProvider.KAKAO,
                SocialId.of(command.kakaoId()),
                toKakaoConsent(command),
                clock);
    }

    private Email toEmail(String email) {
        return (email != null && !email.isBlank()) ? Email.of(email) : null;
    }

    private MemberName toMemberName(String name) {
        return (name != null && !name.isBlank()) ? MemberName.of(name) : null;
    }

    private Gender toGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return Gender.N;
        }
        return Gender.valueOf(gender.toUpperCase(Locale.ROOT));
    }

    private Consent toConsent(RegisterMemberCommand command) {
        boolean privacyConsent = false;
        boolean serviceConsent = false;
        boolean marketingConsent = false;

        for (RegisterMemberCommand.ConsentItem item : command.consents()) {
            switch (item.type().toUpperCase(Locale.ROOT)) {
                case "PRIVACY" -> privacyConsent = item.agreed();
                case "SERVICE" -> serviceConsent = item.agreed();
                case "MARKETING" -> marketingConsent = item.agreed();
                default -> {
                    /* 알 수 없는 동의 유형은 무시 */
                }
            }
        }

        return Consent.of(privacyConsent, serviceConsent, marketingConsent);
    }

    private Consent toKakaoConsent(KakaoOAuthCommand command) {
        if (command.consents() == null || command.consents().isEmpty()) {
            return Consent.of(true, true, false);
        }

        boolean privacyConsent = false;
        boolean serviceConsent = false;
        boolean marketingConsent = false;

        for (KakaoOAuthCommand.ConsentItem item : command.consents()) {
            switch (item.type().toUpperCase(Locale.ROOT)) {
                case "PRIVACY" -> privacyConsent = item.agreed();
                case "SERVICE" -> serviceConsent = item.agreed();
                case "MARKETING" -> marketingConsent = item.agreed();
                default -> {
                    /* 알 수 없는 동의 유형은 무시 */
                }
            }
        }

        return Consent.of(privacyConsent, serviceConsent, marketingConsent);
    }
}
