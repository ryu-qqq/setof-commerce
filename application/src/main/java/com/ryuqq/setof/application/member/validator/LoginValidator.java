package com.ryuqq.setof.application.member.validator;

import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.InvalidCredentialsException;
import com.ryuqq.setof.domain.member.exception.MemberNotActiveException;
import com.ryuqq.setof.domain.member.exception.SocialLoginAlreadyExistsException;
import org.springframework.stereotype.Component;

/**
 * LoginValidator - 로그인 검증기.
 *
 * <p>회원 상태 확인 및 비밀번호 검증을 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class LoginValidator {

    private final MemberReadManager memberReadManager;
    private final PasswordManager passwordManager;

    public LoginValidator(MemberReadManager memberReadManager, PasswordManager passwordManager) {
        this.memberReadManager = memberReadManager;
        this.passwordManager = passwordManager;
    }

    private static final String EMAIL = "EMAIL";

    /**
     * 로그인 검증을 수행합니다.
     *
     * <p>전화번호로 회원을 조회하고, 회원 상태, 소셜 로그인 타입, 비밀번호를 검증합니다.
     *
     * @param phoneNumber 전화번호
     * @param rawPassword 평문 비밀번호
     * @return 검증 통과된 회원 도메인 객체
     * @throws MemberNotActiveException 로그인 불가 상태인 경우
     * @throws SocialLoginAlreadyExistsException 소셜 로그인으로 가입된 회원인 경우
     * @throws InvalidCredentialsException 비밀번호가 일치하지 않는 경우
     */
    public Member validate(String phoneNumber, String rawPassword) {
        MemberWithCredentials credentials =
                memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber);

        Member member = credentials.member();

        if (!member.canLogin()) {
            throw new MemberNotActiveException();
        }

        if (isSocialLoginMember(credentials.socialLoginType())) {
            throw new SocialLoginAlreadyExistsException(credentials.socialLoginType());
        }

        if (!passwordManager.matches(rawPassword, credentials.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return member;
    }

    private boolean isSocialLoginMember(String socialLoginType) {
        return socialLoginType != null
                && !socialLoginType.isBlank()
                && !EMAIL.equals(socialLoginType);
    }
}
