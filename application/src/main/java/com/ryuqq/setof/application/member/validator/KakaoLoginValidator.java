package com.ryuqq.setof.application.member.validator;

import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotActiveException;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * KakaoLoginValidator - 카카오 소셜 로그인 검증기.
 *
 * <p>카카오 로그인 시 탈퇴 회원 차단 및 기존 회원 상태 검증을 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class KakaoLoginValidator {

    private final MemberReadManager memberReadManager;

    public KakaoLoginValidator(MemberReadManager memberReadManager) {
        this.memberReadManager = memberReadManager;
    }

    /**
     * 전화번호로 기존 회원을 조회하고, 존재하면 로그인 가능 상태를 검증합니다.
     *
     * @param phoneNumber 전화번호
     * @return 기존 회원 정보 (Optional)
     * @throws MemberNotActiveException 탈퇴/정지 상태인 경우
     */
    public Optional<MemberWithCredentials> validateAndFindExisting(String phoneNumber) {
        Optional<MemberWithCredentials> credentialsOpt =
                memberReadManager.findWithCredentialsByPhoneNumber(phoneNumber);

        if (credentialsOpt.isPresent()) {
            Member member = credentialsOpt.get().member();
            if (!member.canLogin()) {
                throw new MemberNotActiveException();
            }
        }

        return credentialsOpt;
    }
}
