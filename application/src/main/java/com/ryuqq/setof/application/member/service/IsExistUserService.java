package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.IsExistUserUseCase;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 회원 존재 여부 조회 서비스.
 *
 * <p>IsExistUserUseCase를 구현하며, 전화번호로 회원 가입 여부를 확인합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class IsExistUserService implements IsExistUserUseCase {

    private final MemberReadManager memberReadManager;

    public IsExistUserService(MemberReadManager memberReadManager) {
        this.memberReadManager = memberReadManager;
    }

    @Override
    public IsExistUserResult execute(String phoneNumber) {
        Optional<Member> memberOpt = memberReadManager.findByPhoneNumber(phoneNumber);

        if (memberOpt.isEmpty()) {
            return IsExistUserResult.notJoined();
        }

        Member member = memberOpt.get();
        MemberProfile profile =
                memberReadManager.getProfileByLegacyId(member.legacyMemberIdValue());

        return IsExistUserResult.joined(
                member.legacyMemberIdValue(),
                member.memberNameValue(),
                profile.socialLoginType(),
                member.phoneNumberValue(),
                isSocialLogin(profile.socialLoginType()) ? profile.socialPkId() : "",
                profile.currentMileage(),
                member.createdAt() != null
                        ? LocalDateTime.ofInstant(member.createdAt(), ZoneId.of("Asia/Seoul"))
                                .toString()
                        : null,
                member.isDeleted() ? "Y" : "N");
    }

    private boolean isSocialLogin(String socialLoginType) {
        return socialLoginType != null
                && !socialLoginType.isBlank()
                && !"none".equalsIgnoreCase(socialLoginType)
                && !"EMAIL".equalsIgnoreCase(socialLoginType);
    }
}
