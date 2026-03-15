package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.internal.MyPageReadFacade;
import com.ryuqq.setof.application.member.port.in.GetMyPageUseCase;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

@Service
public class GetMyPageService implements GetMyPageUseCase {

    private final MyPageReadFacade myPageReadFacade;

    public GetMyPageService(MyPageReadFacade myPageReadFacade) {
        this.myPageReadFacade = myPageReadFacade;
    }

    @Override
    public MyPageResult execute(long userId) {
        MemberProfile profile = myPageReadFacade.getProfile(userId);
        Member member = profile.member();

        return new MyPageResult(
                member.idValue(),
                member.phoneNumberValue(),
                member.memberNameValue(),
                member.emailValue(),
                profile.currentMileage(),
                profile.socialLoginType(),
                member.createdAt(),
                profile.orderCounts());
    }
}
