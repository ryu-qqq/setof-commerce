package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.GetUserUseCase;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 회원 프로필 조회 서비스.
 *
 * <p>FetchUserUseCase를 구현하며, MemberReadManager를 통해 회원 프로필을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class GetUserService implements GetUserUseCase {

    private final MemberReadManager memberReadManager;

    public GetUserService(MemberReadManager memberReadManager) {
        this.memberReadManager = memberReadManager;
    }

    @Override
    public UserResult execute(long userId) {
        MemberProfile profile = memberReadManager.getProfileByLegacyId(userId);
        Member member = profile.member();

        return new UserResult(
                member.legacyMemberIdValue(),
                member.phoneNumberValue(),
                member.memberNameValue(),
                member.emailValue(),
                profile.gradeName(),
                profile.currentMileage(),
                profile.socialLoginType());
    }
}
